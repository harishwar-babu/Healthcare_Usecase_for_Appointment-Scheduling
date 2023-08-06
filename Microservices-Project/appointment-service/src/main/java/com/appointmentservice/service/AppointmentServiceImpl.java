package com.appointmentservice.service;
import com.appointmentservice.dto.AppointmentDetailsDto;
import com.appointmentservice.dto.AppointmentResponseDto;
import com.appointmentservice.feign.DoctorFeign;
import com.appointmentservice.feign.NotificationFeign;
import com.appointmentservice.model.AppointmentEntryModel;
import com.appointmentservice.model.DoctorCalendarResponseModel;
import com.appointmentservice.model.DoctorProfileResponseModel;
import com.appointmentservice.repository.AppointmentEntryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
@Service
public class AppointmentServiceImpl implements AppointmentService {
    private static final String SUCCESS_MESSAGE ="Booked";
    @Autowired
    private DoctorFeign doctorFeign;

    @Autowired
    private NotificationFeign notificationFeign;

    @Autowired
    private AppointmentEntryRepository appointmentEntryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int secondsConverter(LocalTime time){
        int hours = time.getHour()*3600;
        int minutes = time.getMinute()*60;
        return hours+minutes;
    }
    @Override
    public boolean doctorAvailability(Integer doctorId,LocalDate date,LocalTime startTime,LocalTime endTime){
        //to check for conflict
        List<AppointmentEntryModel> doctorSlots = appointmentEntryRepository.findAllByDoctorIdAndDate(doctorId,date);
        Collections.sort(doctorSlots, Comparator.comparing(AppointmentEntryModel::getStartTime));
        int patientStartTime = secondsConverter(startTime);
        int patientEndTime = secondsConverter(endTime);
        if(doctorSlots.size()==1){
            int doctorStartTime = secondsConverter(doctorSlots.get(0).getStartTime());
            int doctorEndTime = secondsConverter(doctorSlots.get(0).getEndTime());
            if(doctorStartTime-patientStartTime==0 && doctorEndTime-patientEndTime==0){//same Time
                return false;
            } else if (doctorStartTime<patientStartTime && doctorEndTime>=patientStartTime && patientEndTime>doctorEndTime){ // new Time;
                return true;
            } else if (doctorStartTime>patientStartTime || doctorEndTime>patientEndTime) { // collision
                return false;
            }
        }
        else {
          List<List<Integer>> slots = new ArrayList<>();
          List<Integer> start = new ArrayList<>();
          List<Integer> end = new ArrayList<>();
          for(AppointmentEntryModel doctor:doctorSlots){
              List<Integer> startEnd = new ArrayList<>();
              start.add(secondsConverter(doctor.getStartTime()));
              end.add(secondsConverter(doctor.getEndTime()));
              startEnd.add(secondsConverter(doctor.getStartTime()));
              startEnd.add(secondsConverter(doctor.getEndTime()));
              slots.add(startEnd);
          }

          for(int value=0;value<slots.size()-1;value++){
              int doctorFirstSlotStartTime = slots.get(value).get(0);
              int doctorFirstSlotEndTime = slots.get(value).get(1);
              int doctorSecondSlotStartTime = slots.get(value+1).get(0);
              if(doctorFirstSlotStartTime-patientStartTime==0 && doctorFirstSlotEndTime-patientEndTime==0){
                  //same Time
                  return false;
              } else if (doctorFirstSlotStartTime>patientStartTime && doctorFirstSlotEndTime>patientStartTime) {
                  return false; // as there is collision1 ( 9:30-10:00) and input(9:15-11.00) not for 08:30-9.30
              } else if (patientStartTime>=doctorFirstSlotEndTime && patientEndTime<=doctorSecondSlotStartTime) {
                  return true; // in between (9.30-10.00),(11.00-13.00) and input(10.00-11.00)
              } else if (patientStartTime>=doctorFirstSlotEndTime && patientStartTime>doctorSecondSlotStartTime) {
                  return false; // collision2 (9.30-10.00), (11.00-1.00) -> but the input(10.00-1.00)
              }else if ((patientStartTime<=Collections.min(start) && patientEndTime<=Collections.min(end)) ||(patientStartTime>=Collections.max(start) && patientEndTime>=Collections.max(end))) {
                  return true;
              }
          }
        }
        return true;
    }
    @Override
    public ResponseEntity<AppointmentResponseDto> appointmentBooking(AppointmentDetailsDto appointmentDetails,String token){
        AppointmentResponseDto appointmentResponse = null;
        //Doctor can have only 5 appointments in a Day
        // check the appointment is the FirstEntry
        if(appointmentEntryRepository.findAll().isEmpty()){
            DoctorProfileResponseModel doctorProfileResponseModel = doctorFeign.getAllDoctors(token).stream().filter(doctor->doctor.getAvailability().equals("Y")).findAny().get();
            AppointmentEntryModel appointmentEntryModel = new AppointmentEntryModel();
            appointmentEntryModel.setStartTime(appointmentDetails.getStartTime());
            appointmentEntryModel.setEndTime(appointmentDetails.getEndTime());
            appointmentEntryModel.setDate(appointmentDetails.getDate());
            appointmentEntryModel.setPatientUsername(appointmentDetails.getPatientUsername());
            appointmentEntryModel.setStatus(SUCCESS_MESSAGE);
            appointmentEntryModel.setDoctorId(doctorProfileResponseModel.getId());
            appointmentEntryRepository.save(appointmentEntryModel);
            DoctorCalendarResponseModel doctorCalendarResponseModel = modelMapper.map(appointmentEntryModel,DoctorCalendarResponseModel.class);
            doctorCalendarResponseModel.setDoctorId(doctorProfileResponseModel.getId());
            doctorCalendarResponseModel.setReason(appointmentDetails.getReason());
            appointmentResponse = modelMapper.map(doctorCalendarResponseModel, AppointmentResponseDto.class);
            appointmentResponse.setStatus(SUCCESS_MESSAGE);
            notificationFeign.mailNotification(appointmentDetails.getPatientUsername(),appointmentDetails.getDate());
            return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
        }
        // check if the Date doesn't Exists;
        else if (!appointmentEntryRepository.existsByDate(appointmentDetails.getDate())) {
            HttpHeaders headers = new HttpHeaders();
            DoctorProfileResponseModel doctorProfileResponseModel = doctorFeign.getAllDoctors(token).stream().filter(doctor->doctor.getAvailability().equals("Y")).findAny().get();
            DoctorProfileResponseModel newResponse = new DoctorProfileResponseModel();
            newResponse.setName(doctorProfileResponseModel.getName());
            newResponse.setAddress(doctorProfileResponseModel.getAddress());
            newResponse.setPhone(doctorProfileResponseModel.getPhone());
            newResponse.setEmail(doctorProfileResponseModel.getEmail());
            newResponse.setDate(appointmentDetails.getDate());
            newResponse.setAvailability(doctorProfileResponseModel.getAvailability());
            newResponse.setHealthCareProviderId(doctorProfileResponseModel.getHealthCareProviderId());
            AppointmentEntryModel appointmentEntryModel = new AppointmentEntryModel();
            appointmentEntryModel.setStartTime(appointmentDetails.getStartTime());
            appointmentEntryModel.setEndTime(appointmentDetails.getEndTime());
            appointmentEntryModel.setDate(appointmentDetails.getDate());
            appointmentEntryModel.setPatientUsername(appointmentDetails.getPatientUsername());
            String message = doctorFeign.saveDoctor(newResponse);
            appointmentEntryModel.setStatus(message);
            appointmentEntryModel.setDoctorId(doctorProfileResponseModel.getId());
            appointmentEntryRepository.save(appointmentEntryModel);
            DoctorCalendarResponseModel doctorCalendarResponseModel = modelMapper.map(appointmentEntryModel,DoctorCalendarResponseModel.class);
            doctorCalendarResponseModel.setDoctorId(doctorProfileResponseModel.getId());
            doctorCalendarResponseModel.setReason(appointmentDetails.getReason());
            appointmentResponse = modelMapper.map(doctorCalendarResponseModel, AppointmentResponseDto.class);
            appointmentResponse.setStatus(SUCCESS_MESSAGE);
            notificationFeign.mailNotification(appointmentDetails.getPatientUsername(),appointmentDetails.getDate());
            return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);

        }//conflict check and Other Scenario
        else if (!appointmentEntryRepository.findAll().isEmpty() && appointmentEntryRepository.existsByDate(appointmentDetails.getDate())) {
            for(DoctorProfileResponseModel doctor:doctorFeign.getAllDoctors(token)){
                if(appointmentEntryRepository.existsByDoctorId(doctor.getId()) &&
                        doctorAvailability(doctor.getId(),appointmentDetails.getDate(),appointmentDetails.getStartTime(),appointmentDetails.getEndTime()) &&
                appointmentEntryRepository.findAllByDoctorIdAndDate(doctor.getId(),appointmentDetails.getDate()).size()<5 && doctor.getAvailability().equals("Y")){
                    AppointmentEntryModel appointmentEntry= new AppointmentEntryModel();
                    appointmentEntry.setStartTime(appointmentDetails.getStartTime());
                    appointmentEntry.setEndTime(appointmentDetails.getEndTime());
                    appointmentEntry.setDate(appointmentDetails.getDate());
                    appointmentEntry.setPatientUsername(appointmentDetails.getPatientUsername());
                    appointmentEntry.setStatus(SUCCESS_MESSAGE);
                    appointmentEntry.setDoctorId(doctor.getId());
                    appointmentEntryRepository.save(appointmentEntry);
                    DoctorCalendarResponseModel doctorCalendar = modelMapper.map(appointmentEntry,DoctorCalendarResponseModel.class);
                    doctorCalendar.setDoctorId(doctor.getId());
                    doctorCalendar.setReason(appointmentDetails.getReason());
                    appointmentResponse= modelMapper.map(doctorCalendar, AppointmentResponseDto.class);
                    appointmentResponse.setStatus(SUCCESS_MESSAGE);
                    notificationFeign.mailNotification(appointmentDetails.getPatientUsername(),appointmentDetails.getDate());
                    return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
                } else if (!appointmentEntryRepository.existsByDoctorId(doctor.getId())) {
                    AppointmentEntryModel appointmentEntry= new AppointmentEntryModel();
                    appointmentEntry.setStartTime(appointmentDetails.getStartTime());
                    appointmentEntry.setEndTime(appointmentDetails.getEndTime());
                    appointmentEntry.setDate(appointmentDetails.getDate());
                    appointmentEntry.setPatientUsername(appointmentDetails.getPatientUsername());
                    appointmentEntry.setStatus(SUCCESS_MESSAGE);
                    appointmentEntry.setDoctorId(doctor.getId());
                    appointmentEntryRepository.save(appointmentEntry);
                    DoctorCalendarResponseModel doctorCalendar = modelMapper.map(appointmentEntry,DoctorCalendarResponseModel.class);
                    doctorCalendar.setDoctorId(doctor.getId());
                    doctorCalendar.setReason(appointmentDetails.getReason());
                    appointmentResponse= modelMapper.map(doctorCalendar, AppointmentResponseDto.class);
                    appointmentResponse.setStatus(SUCCESS_MESSAGE);
                    notificationFeign.mailNotification(appointmentDetails.getPatientUsername(),appointmentDetails.getDate());
                    break;
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
    }
}