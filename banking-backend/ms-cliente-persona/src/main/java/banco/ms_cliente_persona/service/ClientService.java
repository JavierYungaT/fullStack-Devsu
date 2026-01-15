package banco.ms_cliente_persona.service;

import banco.ms_cliente_persona.dto.ResponseData;
import banco.ms_cliente_persona.dto.client.ClientDTO;
import banco.ms_cliente_persona.dto.client.UserDniDTO;
import banco.ms_cliente_persona.dto.client.UserIdDTO;
import banco.ms_cliente_persona.exceptions.CustomValidationException;

import java.util.List;
import java.util.Map;

public interface ClientService {

    List<ClientDTO> getAllClients();
    ClientDTO getUserById(UserIdDTO userIdDTO) throws CustomValidationException;
    ResponseData saveClient(ClientDTO clientDTO) throws CustomValidationException;
    ResponseData updateClient(Integer userId, Map<String, Object> fields) throws CustomValidationException;
    ResponseData deleteClient(UserDniDTO userDniDTO) throws CustomValidationException;
    ResponseData updateClientAllData(ClientDTO clientDTO) throws CustomValidationException;
}
