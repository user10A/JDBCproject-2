package Dao.Impl;

import Congif.Config;
import Dao.JobDao;
import Model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {
    private final Connection connection = Config.getConnection();
    @Override
    public void createJobTable() {
        String sql=
                "CREATE TABLE IF NOT EXISTS \"Job\"(" +
            "id serial primary key ," +
                        "position varchar," +
                        "profession varchar," +
                        "description varchar," +
                        "experience int ) ";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Table Employee successfully created ");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJob(Job job) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement= connection.prepareStatement("""
    insert into "Job"(position,profession,description,experience)
    values (?,?,?,?)
    """);
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4, job.getExperience());
            preparedStatement.executeUpdate();
            System.out.println("job successfully added");
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Job getJobById(Long jobId) {
        Job job = new Job();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM \"Job\" WHERE id = ?");
            preparedStatement.setLong(1, jobId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { // Проверка на наличие данных
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setExperience(resultSet.getInt("experience"));
                job.setDescription(resultSet.getString("description"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return job;
    }


    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job>jobs = new ArrayList<>();
        String sql = "SELECT * FROM \"Job\" ORDER BY experience " + ascOrDesc;
        try (PreparedStatement preparedStatement= connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                jobs.add(new Job(
                        resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job = new Job();
        String sql =
                "SELECT j.id, j.position, j.profession, j.description, j.experience " +
                        "FROM \"Employee\" e " +
                        "INNER JOIN \"Job\" j ON e.job_Id = j.id " +
                        "WHERE e.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return job;
    }


    @Override
    public void deleteDescriptionColumn() {
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("""
delete column description from "Job"
""");
            System.out.println("column description successfully deleted  ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
