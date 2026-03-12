@RestController
@RequestMapping("/actions")
public class ActionController {

    @Autowired
    private ActionService service;

    @GetMapping
    public List<Action> getAll(){
        return service.getAllActions();
    }

    @PostMapping
    public Action create(@RequestBody Action action){
        return service.save(action);
    }
}