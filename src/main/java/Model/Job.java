package Model;

public class Job {
    private Long id;
    private String position; //("Mentor","Management","Instructor") ушундай маанилер берилсин
    private String profession; //("Java","JavaScript")
    private String description;//("Backend developer","Fronted developer")
    private int experience; //(1,2,3........) опыт работы

    public Job() {
    }

    public Job(Long id, String position, String profession, String description, int experience) {
        this.id = id;
        this.position = position;
        this.profession = profession;
        this.description = description;
        this.experience = experience;
    }

    public Job(String position, String profession, String description, int experience) {
        this.position = position;
        this.profession = profession;
        this.description = description;
        this.experience = experience;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", profession='" + profession + '\'' +
                ", description='" + description + '\'' +
                ", experience=" + experience +
                '}';
    }
}
