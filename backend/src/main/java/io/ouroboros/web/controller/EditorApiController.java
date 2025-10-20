package io.ouroboros.web.controller;

import io.ouroboros.global.APIResponse;
import io.ouroboros.web.dto.ApiDefinition;
import io.ouroboros.web.dto.Endpoint;
import io.ouroboros.api.service.ApiDefinitionService;
import io.ouroboros.mock.service.DummyDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ourohead/api")
@RequiredArgsConstructor
public class EditorApiController {

    private final ApiDefinitionService apiDefinitionService;
    private final DummyDataGenerator dummyDataGenerator;

    /**
     * Retrieves the current API definition configured for the application.
     *
     * @return a ResponseEntity containing the APIResponse with current ApiDefinition and HTTP 200 OK status.
     */
    @GetMapping("/definition")
    public ResponseEntity<APIResponse<ApiDefinition, Void>> getApiDefinition() {
        log.info("Fetching current API definition");
        ApiDefinition definition = apiDefinitionService.loadApiDefinition();
        return ResponseEntity.ok(APIResponse.success("API 정의를 성공적으로 조회했습니다.", definition));
    }

    /**
     * Saves the provided API definition and reloads the application's endpoints.
     *
     * @param apiDefinition the API definition to persist and apply
     * @return a response entity containing APIResponse; on success returns success response with message,
     *         on error returns failure response with error code and message
     */
    @PostMapping("/definition")
    public ResponseEntity<APIResponse<Void, Void>> saveApiDefinition(@RequestBody ApiDefinition apiDefinition) {
        log.info("Saving API definition with {} endpoints",
                apiDefinition.getEndpoints() != null ? apiDefinition.getEndpoints().size() : 0);

        try {
            apiDefinitionService.saveApiDefinition(apiDefinition);
            apiDefinitionService.reloadEndpoints();
            return ResponseEntity.ok(
                APIResponse.success("API 정의가 저장되고 엔드포인트가 다시 로드되었습니다.", null)
            );
        } catch (Exception e) {
            log.error("Failed to save API definition", e);
            return ResponseEntity.internalServerError()
                .body(APIResponse.fail("SAVE_ERROR", "API 정의 저장에 실패했습니다: " + e.getMessage()));
        }
    }

    /**
     * Generate a preview payload for the given endpoint.
     *
     * Attempts to produce sample response data by preferring a response with a 2xx status code;
     * if no such response exists the first response is used. If a response body is defined,
     * returns generated dummy data; if no suitable response is defined returns a success response with null data;
     * on failure returns an error response with error code and message.
     *
     * @param endpoint the endpoint definition to preview; may contain multiple response definitions
     * @return APIResponse containing the preview data on success, or error information on failure
     */
    @PostMapping("/preview")
    public ResponseEntity<APIResponse<Object, Void>> previewEndpoint(@RequestBody Endpoint endpoint) {
        log.info("Generating preview for endpoint: {} {}", endpoint.getMethod(), endpoint.getPath());

        try {
            // responses 배열에서 첫 번째 성공 응답(2xx)을 미리보기로 사용
            if (endpoint.getResponses() != null && !endpoint.getResponses().isEmpty()) {
                var successResponse = endpoint.getResponses().stream()
                        .filter(sr -> sr.getStatusCode() != null && sr.getStatusCode() >= 200 && sr.getStatusCode() < 300)
                        .findFirst()
                        .orElse(endpoint.getResponses().get(0)); // 성공 응답 없으면 첫 번째 응답 사용

                if (successResponse.getResponse() != null) {
                    Object previewData = dummyDataGenerator.generateDummyData(successResponse.getResponse());
                    return ResponseEntity.ok(
                        APIResponse.success("미리보기 데이터를 성공적으로 생성했습니다.", previewData)
                    );
                }
            }

            return ResponseEntity.ok(
                APIResponse.success("정의된 응답이 없습니다.", null)
            );
        } catch (Exception e) {
            log.error("Failed to generate preview", e);
            return ResponseEntity.internalServerError()
                .body(APIResponse.fail("PREVIEW_ERROR", "미리보기 생성에 실패했습니다: " + e.getMessage()));
        }
    }
}