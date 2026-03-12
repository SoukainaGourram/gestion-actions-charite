@Entity
public class Donation {

    @Id
    @GeneratedValue
    private Long id;

    private Double amount;
}