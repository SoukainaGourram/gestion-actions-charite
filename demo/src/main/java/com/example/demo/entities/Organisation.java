@Entity
public class Organisation {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private String fiscalNumber;
}