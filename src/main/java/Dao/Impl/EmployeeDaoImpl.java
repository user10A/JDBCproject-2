package Dao.Impl;

import Congif.Config;
import Dao.EmployeeDao;
import Model.Employee;
import Model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    private final Connection connection = Config.getConnection();
    @Override
    public void createEmployee() {
        Statement statement = null;
        String sql=
                "CREATE TABLE IF NOT EXISTS \"Employee\"(" +
                        "id serial primary key," +
                        "firstName varchar," +
                        "lastName varchar ," +
                        "age int," +
                        "email varchar unique," +
                        "job_Id int references \"Job\"(id) )";
        try {
            statement= connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Table Employee successfully created");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement= connection.prepareStatement("""
        insert into "Employee"(firstName,lastName,age,email,job_id)
        values (?,?,?,?,?)
        """);
            preparedStatement.setString(1,employee.getFirstName());
            preparedStatement.setString(2,employee.getLastName());
            preparedStatement.setInt(3,employee.getAge());
            preparedStatement.setString(4,employee.getEmail());
            preparedStatement.setInt(5,employee.getJobId());

            System.out.println("Employee successfully added");

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropTable() {
        Statement statement = null;
        try {
            statement= connection.createStatement();
            statement.executeUpdate("""
drop table IF exists"Employee"
""");
            System.out.println("Employee table successfully Dropping");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cleanTable() {
        Statement statement = null;
        try {
            statement= connection.createStatement();
            statement.executeUpdate("""
DELETE FROM "Employee"
""");
            System.out.println("Employee Table successfully cleaning");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement= connection.prepareStatement(
    "UPDATE \"Employee\" SET firstName=?, lastName=?, age=?, email=?, job_id=? WHERE id=? "
    );
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.setLong(6, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee>employees =new ArrayList<>();
        Statement statement = null;
        try {
            statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("""
select * from "Employee"
""");
            while(resultSet.next()){
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));

            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = new Employee();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM \"Employee\" WHERE email=?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("firstName"));
                employee.setLastName(resultSet.getString("lastName"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return employee;
    }


    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee, Job> map = new LinkedHashMap<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("""
            SELECT e.id AS e_id, e.firstName, e.lastName, e.age, e.email,
                   j.id AS j_id, j.position, j.profession, j.experience, j.description
            FROM "Employee" e
            INNER JOIN "Job" j ON e.job_id = j.id
            WHERE e.id = ?
        """);
            preparedStatement.setLong(1, employeeId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e_id"));
                employee.setFirstName(resultSet.getString("firstName"));
                employee.setLastName(resultSet.getString("lastName"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));

                Job job = new Job();
                job.setId(resultSet.getLong("j_id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setExperience(resultSet.getInt("experience"));
                job.setDescription(resultSet.getString("description"));

                map.put(employee, job);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return map;
    }


    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("""
            SELECT e.id, e.firstName, e.lastName, e.age, e.email, e.job_id
            FROM "Employee" e
            INNER JOIN "Job" j ON e.job_id = j.id
            WHERE j.position = ?
        """);
            preparedStatement.setString(1, position);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return employees;
    }

}
