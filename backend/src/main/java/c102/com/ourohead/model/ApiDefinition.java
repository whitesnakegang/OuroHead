package c102.com.ourohead.model;

import lombok.Data;
import java.util.List;

@Data
public class ApiDefinition {
    private List<Endpoint> endpoints;
}
