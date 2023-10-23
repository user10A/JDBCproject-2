package peaksoft;

import Congif.Config;
import Dao.Impl.EmployeeDaoImpl;
import Model.Employee;
import Model.Job;
import Service.EmployeeService;
import Service.Impl.EmployeeServiceImpl;
import Service.Impl.JobServiceImpl;
import Service.JobService;

import java.util.Scanner;

import static java.lang.System.in;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Config.getConnection();
        EmployeeService employeeService = new EmployeeServiceImpl() ;
        JobService jobService= new JobServiceImpl() ;
        while (true) {
            switch (new Scanner(System.in).nextLine()) {
                case "1" -> jobService.createJobTable();
                case "2" -> {
                    Job job = new Job("junior Developer","It","develop bekend java",1);
                    jobService.addJob(job);
                }
                case "3" -> System.out.println(jobService.getJobById(1L));
                case "4" -> System.out.println(jobService.sortByExperience("asc"));
                case "5" -> System.out.println(jobService.sortByExperience("desc"));
                case "6" -> System.out.println(jobService.getJobByEmployeeId(1L));
                case "7" -> jobService.deleteDescriptionColumn();
                case "8", "save" -> employeeService.createEmployee();
                case "9" -> {
                    Employee employee = new Employee("erkin","toigonbaev",21,"erin@gmail.com",1);
                    employeeService.addEmployee(employee);
                }
                case "10" -> employeeService.dropTable();
                case "11" -> {
                    Employee employee = new Employee("Erkin","Toigonbaev",20,"Er@gmail.com",1);
                    employeeService.updateEmployee(1L,employee);
                }
                case "12" -> employeeService.cleanTable();
                case "13" -> System.out.println(employeeService.getAllEmployees());
                case "14" -> System.out.println(employeeService.findByEmail("Er@gmail.com"));
                case "15" -> System.out.println(employeeService.getEmployeeById(1L));
                case "16" -> System.out.println(employeeService.getEmployeeByPosition("junior Developer"));
            }
        }
    }
}
