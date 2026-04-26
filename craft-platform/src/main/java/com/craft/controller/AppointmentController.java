package com.craft.controller;

import com.craft.entity.Appointment;
import com.craft.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@CrossOrigin
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Appointment create(@RequestBody Appointment appointment) {
        return appointmentService.create(appointment);
    }

    @GetMapping
    public List<Appointment> findAll() {
        return appointmentService.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Appointment> findByUserId(@PathVariable Long userId) {
        return appointmentService.findByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public List<Appointment> findByStatus(@PathVariable String status) {
        return appointmentService.findByStatus(status);
    }

    @PutMapping
    public Appointment update(@RequestBody Appointment appointment) {
        return appointmentService.update(appointment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }

    @PutMapping("/status/{id}")
    public Appointment updateStatus(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.updateStatus(id, appointment.getStatus());
    }
}