# Appointment Scheduler

A web-based appointment scheduling application built with Spring Boot that allows users to create, manage, and book appointments.

**Deployed at:** https://appointment-scheduler-h5cg.onrender.com/

## Features

### User Management
- **User Login**: Secure login with email and password authentication
- **Role-Based Access**: Support for two user roles - Professor and Student with different permissions

### Appointment Creation
- **Single Day Scheduling**: Create multiple appointment slots within a single day
- **Multi-Day Scheduling**: Create appointment slots across multiple days with flexible time ranges for each day
- **Automatic Slot Generation**: Automatically generates time slots based on:
  - Start and end time for the appointment window
  - Duration for each appointment slot
  - Optional gap time between consecutive slots
  - Automatic slot numbering (Slot 1, Slot 2, etc.)

### Appointment Types
- **Individual Appointments**: One student can book only one slot per appointment group
- **Group Appointments**: Allows group-based booking with validation to ensure:
  - Only members assigned to a group can book
  - Only one slot per group can be booked in an appointment group
  - Prevents multiple group members from booking different slots in the same appointment group

### Booking System
- **View Appointments**: Students can view all available appointment slots
- **Book Appointments**: Students can book available slots with validation:
  - Prevents double booking
  - Enforces one booking per appointment group
  - Group appointment restrictions
- **Booking Status**: Track which appointments are booked

### Cancellation
- **Role-Based Cancellation**:
  - **Professors**: Can cancel any appointment slot at any time
  - **Students**: Can cancel only their own bookings with time restrictions
- **Time-Based Restrictions**: Students can only cancel before the first appointment in the group starts
- **Slot Release**: Cancelled slots become available for other students to book

### Appointment Management
- **View All Appointments**: List all appointment slots with booking status
- **Edit Appointments**: Update appointment details (title, description, location) for entire appointment groups - only allowed when no slots in the group have been booked
- **Delete Appointment Groups**: Remove all slots in an appointment group - only allowed when no slots in the group have been booked
- **Booking Protection**: Edit and delete operations are automatically disabled once any slot in an appointment group is booked, preventing disruption of confirmed appointments
- **Group-Based Operations**: All operations work on appointment groups to maintain consistency

