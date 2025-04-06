package com.example.demo.Controller;


import com.example.demo.Model.Restore.RestoreRequest;
import com.example.demo.Model.Restore.RestoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/restore")
@CrossOrigin(origins = "*")
public class RestoreController {

    private final RestoreService restoreService;

    public RestoreController(RestoreService restoreService) {
        this.restoreService = restoreService;
    }

    @PostMapping(value = "/backup", consumes = "application/json")
    public ResponseEntity<Map<String, String>> restoreBackup(@RequestBody RestoreRequest restoreRequest){
        Map<String, String> response = new HashMap<>();
        response.put("message", "Restore done");
        System.out.println(restoreService.restore(restoreRequest));
        return ResponseEntity.ok(response);
    }
}
