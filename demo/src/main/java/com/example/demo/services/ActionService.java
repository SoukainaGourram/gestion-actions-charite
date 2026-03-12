@Service
public class ActionService {

    @Autowired
    private ActionRepository repo;

    public List<Action> getAllActions(){
        return repo.findAll();
    }

    public Action save(Action action){
        return repo.save(action);
    }
}