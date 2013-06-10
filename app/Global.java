import play.Application;
import play.GlobalSettings;
import service.MapService;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        MapService.retrieveMap();
    }
}
