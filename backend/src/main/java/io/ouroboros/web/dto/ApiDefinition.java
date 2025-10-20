package io.ouroboros.web.dto;

import lombok.Data;
import java.util.List;

@Data
public class ApiDefinition {
    private List<Endpoint> endpoints;
}
