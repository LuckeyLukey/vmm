package com.example.vmm;

import com.example.vmm.domain.Grade;
import com.example.vmm.domain.Student;
import com.example.vmm.domain.Subject;
import com.example.vmm.persistance.GradeRepository;
import com.example.vmm.persistance.StudentRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.persistence.PostLoad;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/th")
public record Controller(GradeRepository gradeRepository, StudentRepository studentRepository) {
    static boolean initiated=false;
    @GetMapping("/students")
    public String getStudentOverview(Model model){
        if (!initiated)
            gradeRepository.saveAll(getGradesFromCSV());

        Student student=studentRepository.getReferenceById(20200473);
        model.addAttribute("students",studentRepository.findAll());
        return "overview";
    }

    @GetMapping("/grades/{id}")
    public String getGradesForStudent(Model model, @PathVariable("id") Integer id){
        Student student=studentRepository.getReferenceById(id);
        model.addAttribute("student",student);
        model.addAttribute("newGrade",new Grade());
        model.addAttribute("possibleSubjects",Subject.values());
        model.addAttribute("possibleGrades", new int[]{1,2,3,4,5});

        return "grades";
    }

    @PostMapping("/grades/{id}")
    public String addGrade(@PathVariable("id") Integer id,Grade newGrade,Model model){
        newGrade.setStudent(studentRepository.getReferenceById(id));
        gradeRepository.save(newGrade);
        return "redirect:/th/students";
    }

    public List<Grade> getGradesFromCSV(){
        List<Grade> grades = new ArrayList<>();
        String line="";
        String[] splitLine;
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/grades.csv"))){
            while ((line = br.readLine()) != null){
                try {
                    splitLine = line.split(",");
                    Grade grade = new Grade(
                            studentRepository.getReferenceById(Integer.valueOf(splitLine[0])),
                            LocalDate.parse(splitLine[1]),
                            Subject.valueOf(splitLine[2]),
                            Integer.parseInt(splitLine[3]));
                    //grade.setStudent(studentRepository.getReferenceById(Integer.valueOf(splitLine[0])));
                    //grade.setAssignedDate(Date.valueOf(splitLine[1]));
                    //grade.setSubject(Subject.valueOf(splitLine[2]));
                    //grade.setGrade(Integer.parseInt(splitLine[3]));
                    grades.add(grade);
                }catch (Exception e){
                    System.out.println("na");
                }
            }
        }catch(Exception e){
            System.out.println("sicha nd");
        }
        initiated=true;
        return grades;
    }


}
