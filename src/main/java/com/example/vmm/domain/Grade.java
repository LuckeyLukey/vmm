package com.example.vmm.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="grades")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Grade {
    public Grade(Student student, LocalDate assignedDate, Subject subject, int grade) {
        this.student = student;
        this.assignedDate = assignedDate;
        this.subject = subject;
        this.gradeValue = grade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="gradeId")
    private Integer id;

    @ManyToOne
    private Student student;

    //@Column(name="assigned_date")
    private LocalDate assignedDate;

    //@Column(name="subject")
    @Enumerated
    private Subject subject;

    //@Column(name="grade")
    //@Min(1)
    //@Max(5)
    private Integer gradeValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Grade{" + id +
                ",student=" + student +
                ", assignedDate=" + assignedDate +
                ", subject=" + subject +
                ", grade=" + gradeValue +
                '}';
    }
}
