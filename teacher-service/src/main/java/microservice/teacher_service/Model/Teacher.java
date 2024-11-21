package microservice.teacher_service.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import microservice.common_classes.Models.Person;
import microservice.common_classes.Utils.Teacher.Title;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "teacher")
public class Teacher extends Person {
    @Column(name = "title")
    private Title title;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "hired_at")
    private LocalDateTime hired_at;

    @Column(name = "salary_per_month")
    private double salaryPerMonth;

    /*
    CURP is the “Unique Population Registry Code” assigned to each person
    of Mexican nationality, whether by birth or naturalization, as well as
    to foreign persons residing in Mexico.
     */
    @Column(name = "curp", nullable = false, unique = true, length = 18)
    private String curp;

    /**
     * RFC (Registro Federal de Contribuyentes):
     * A 13-character fiscal identifier used in Mexico for individuals,
     * representing the taxpayer’s identity.
     * Format example: ABCD900101XXX
     */
    @Column(name = "rfc", length = 13, unique = true, nullable = false)
    private String rfc;

    /**
     * NSS (Número de Seguro Social):
     * An 11-digit number assigned by the Mexican Social Security Institute (IMSS).
     * It identifies employees in Mexico for social security purposes.
     * Format example: 12345678901
     */
    @Column(name = "nss", length = 11, unique = true, nullable = false)
    private String nss;
}
