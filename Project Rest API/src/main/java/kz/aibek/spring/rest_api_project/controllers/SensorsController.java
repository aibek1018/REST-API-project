package kz.aibek.spring.rest_api_project.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import kz.aibek.spring.rest_api_project.dto.SensorDTO;
import kz.aibek.spring.rest_api_project.models.Sensor;
import kz.aibek.spring.rest_api_project.services.SensorService;
import kz.aibek.spring.rest_api_project.util.MeasurementErrorResponse;
import kz.aibek.spring.rest_api_project.util.MeasurementException;
import kz.aibek.spring.rest_api_project.util.SensorValidator;

import javax.validation.Valid;

import static kz.aibek.spring.rest_api_project.util.ErrorsUtil.returnErrorsToClient;


@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorsController(SensorService sensorService, ModelMapper modelMapper,
                             SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        Sensor sensorToAdd = convertToSensor(sensorDTO);

        sensorValidator.validate(sensorToAdd, bindingResult);

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        sensorService.register(sensorToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
