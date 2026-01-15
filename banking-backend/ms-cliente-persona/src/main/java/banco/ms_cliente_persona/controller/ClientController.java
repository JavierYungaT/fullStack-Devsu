package banco.ms_cliente_persona.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import banco.ms_cliente_persona.dto.ResponseData;
import banco.ms_cliente_persona.dto.client.ClientDTO;
import banco.ms_cliente_persona.dto.client.UserDniDTO;
import banco.ms_cliente_persona.dto.client.UserIdDTO;
import banco.ms_cliente_persona.exceptions.CustomValidationException;
import banco.ms_cliente_persona.service.ClientService;

import java.util.List;
import java.util.Map;

//@CrossOrigin(
//        origins = "http://localhost:4200",
//        allowedHeaders = "*",
//        methods = { RequestMethod.GET, RequestMethod.POST,
//                RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE,
//                RequestMethod.OPTIONS }
//)
@RestController
@RequestMapping("/clientes")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/findAllClients")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/findClientById")
    public ResponseEntity<ClientDTO> getCompanyById(@RequestBody @Valid UserIdDTO userIdDTO) throws CustomValidationException, CustomValidationException, CustomValidationException {
        ClientDTO client = clientService.getUserById(userIdDTO);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/saveClient")
    public ResponseEntity<ResponseData> saveClient(@RequestBody @Valid ClientDTO clientDTO) throws CustomValidationException {
        ResponseData client = clientService.saveClient(clientDTO);
        return ResponseEntity.ok(client);
    }

    @PatchMapping("/updateClient/{userId}")
    public ResponseEntity<ResponseData> updateClient(@PathVariable Integer userId, @RequestBody Map<String, Object> fields) throws CustomValidationException {
        ResponseData client = clientService.updateClient(userId, fields);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/updateClientAllData")
    public ResponseEntity<ResponseData> updateClientAllData(@RequestBody @Valid ClientDTO clientDTO) throws CustomValidationException {
        ResponseData client = clientService.updateClientAllData(clientDTO);
        return ResponseEntity.ok(client);
    }

//    @DeleteMapping("/deleteClient")
//    public ResponseEntity<ResponseData> deleteClient(@RequestBody @Valid UserDniDTO userDniDTO) throws CustomValidationException {
//        ResponseData client = clientService.deleteClient(userDniDTO);
//        return ResponseEntity.ok(client);
//    }

    @DeleteMapping("/deleteClient/{dni}")
    public ResponseEntity<ResponseData> deleteClient(@PathVariable String dni)
            throws CustomValidationException {

        ResponseData client = clientService.deleteClient(
                new UserDniDTO(dni)
        );

        return ResponseEntity.ok(client);
    }


}
