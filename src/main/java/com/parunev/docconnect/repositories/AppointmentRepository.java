package com.parunev.docconnect.repositories;

import com.parunev.docconnect.models.Appointment;
import com.parunev.docconnect.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
/**
 * The {@code AppointmentRepository} interface provides database access methods for managing appointments
 * in the DocConnect application. It extends the {@link org.springframework.data.jpa.repository.JpaRepository}
 * interface, which provides basic CRUD (Create, Read, Update, Delete) operations for the {@link Appointment} entity.
 *
 * <p>This repository offers several custom query methods for retrieving appointments based on various criteria.
 * These methods allow users to search for appointments by user ID, specialist ID, appointment status, and apply
 * filters based on specialist name, specialty ID, date range, and user ID.
 *
 * <p>Additionally, there are methods that support pagination for retrieving upcoming and completed appointments
 * with filtering options.
 *
 * <p>The {@code AppointmentRepository} interface plays a crucial role in managing and retrieving appointment data
 * within the DocConnect application.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByUserId(Long userId);

    List<Appointment> findAllBySpecialistId(Long specialistId);

    List<Appointment> findAllByUserIdAndSpecialistId(Long userId, Long specialistId);

    List<Appointment> findAllByAppointmentStatus(Status status);

    @Query("SELECT a FROM APPOINTMENTS a " +
            "WHERE a.appointmentStatus = 'STATUS_UPCOMING'" +
            "AND (:specialistName IS NULL OR " +
            "LOWER(CONCAT(a.specialist.firstName, ' ', a.specialist.lastName)) LIKE %:specialistName%) " +
            "AND (:specialtyId IS NULL OR a.specialist.specialty.id = :specialtyId) " +
            "AND (:fromDate IS NULL OR a.dateTime >= :fromDate) " +
            "AND (:toDate IS NULL OR a.dateTime <= :toDate)" +
            "AND (:userId IS NULL OR a.user.id = :userId)")
    Page<Appointment> findUpcomingAppointments(
            @Param("specialistName") String specialistName,
            @Param("specialtyId") Long specialtyId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT a FROM APPOINTMENTS a " +
            "WHERE a.appointmentStatus = 'STATUS_COMPLETED'" +
            "AND (:specialistName IS NULL OR " +
            "LOWER(CONCAT(a.specialist.firstName, ' ', a.specialist.lastName)) LIKE %:specialistName%) " +
            "AND (:specialtyId IS NULL OR a.specialist.specialty.id = :specialtyId) " +
            "AND (:fromDate IS NULL OR a.dateTime >= :fromDate) " +
            "AND (:toDate IS NULL OR a.dateTime <= :toDate)" +
            "AND (:userId IS NULL OR a.user.id = :userId)")
    Page<Appointment> findCompletedAppointments(
            @Param("specialistName") String specialistName,
            @Param("specialtyId") Long specialtyId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("userId") Long userId,
            Pageable pageable
    );
}
