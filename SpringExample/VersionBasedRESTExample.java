package kr.cbnu.lesson10;

import kr.cbnu.lesson10.dto.TestDto;
import kr.cbnu.lesson10.model.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v2")
public class VersionBasedRESTExample {
    private TestService service;

    public VersionBasedRESTExample(TestService service) {
        this.service = service;
    }

    @GetMapping("/status/toggle/")
    public ResponseEntity<Boolean> toggleStatus() {
        try {
            service.toggleApi();
            return ResponseEntity.ok(service.isApiEndpointEnabled());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/visit")
    public ResponseEntity<TestDto> test() {
        try {
            if (service.isApiEndpointEnabled()) {
                int visitCount = service.getVisitCount(true);
                TestDto dto = new TestDto(visitCount);
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
