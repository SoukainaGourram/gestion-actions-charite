@Service
public class DonationService {

    private final DonationRepository repo;

    public DonationService(DonationRepository repo) {
        this.repo = repo;
    }

    public Donation save(Donation donation) {
        return repo.save(donation);
    }
}