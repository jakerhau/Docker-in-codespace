import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalRestService {

    private CalService calService;

    private final List<CalResultVO> history = new ArrayList<>();

    public void setCalService(CalService calService) {
        this.calService = calService;
    }

    @POST
    public CalResultVO calc(CalInputVO input) {
        CalResultVO result = calService.cal(input);
        history.add(result);
        return result;
    }

    @GET
    @Path("/all")
    public List<CalResultVO> getHistory() {
        return history;
    }
}
