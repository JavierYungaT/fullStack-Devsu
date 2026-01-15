package banco.ms_cliente_persona.service.impl;


import banco.ms_cliente_persona.dto.ResponseData;
import banco.ms_cliente_persona.dto.client.ClientAccountEventDTO;
import banco.ms_cliente_persona.dto.client.ClientDTO;
import banco.ms_cliente_persona.dto.client.UserDniDTO;
import banco.ms_cliente_persona.dto.client.UserIdDTO;
import banco.ms_cliente_persona.enums.ErrorMessagesEnum;
import banco.ms_cliente_persona.enums.SuccessCodesEnum;
import banco.ms_cliente_persona.enums.SuccessMessagesEnum;
import banco.ms_cliente_persona.exceptions.CustomValidationException;
import banco.ms_cliente_persona.exceptions.ResourceNotFoundException;
import banco.ms_cliente_persona.model.Client;
import banco.ms_cliente_persona.model.Person;
import banco.ms_cliente_persona.repository.ClientRepository;
import banco.ms_cliente_persona.repository.PersonRepository;
import banco.ms_cliente_persona.service.ClientService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ClientEventService clientEventService;


    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public ClientDTO getUserById(UserIdDTO userIdDTO) {
        return clientRepository.findById(userIdDTO.getUserId())
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage()));
    }

    @Transactional
    public ResponseData saveClient(ClientDTO clientDTO) throws CustomValidationException {

        Optional<Person> existingPerson = personRepository.findByPersonDni(clientDTO.getPersonDni());
        if (existingPerson.isPresent()) {
            throw new CustomValidationException(ErrorMessagesEnum.CLIENT_ALREADY_EXISTS.getMessage());
        }
        Client newClient = new Client();
        modelMapper.map(clientDTO, newClient);
        Client clientSaved = clientRepository.save(newClient);
        ClientAccountEventDTO client = new ClientAccountEventDTO(
                clientSaved.getPersonDni(),
                clientSaved.getPersonName(),
                clientDTO.getTipoCuenta(),
                clientDTO.getSaldoInicial()
        );
        clientEventService.publish(client);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_CREATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData updateClient(Integer userId, Map<String, Object> fields)  {

        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage()));

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Client.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, client, value);
            } else {
                throw new ResourceNotFoundException(ErrorMessagesEnum.FIELD_NOT_FOUND.getMessage() + ": " + key);
            }
        });
        clientRepository.save(client);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );

    }

    public ResponseData updateClientAllData(ClientDTO clientDTO)  {
        Person existingPerson = personRepository.findByPersonDni(clientDTO.getPersonDni())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage()));


        existingPerson.setPersonName(clientDTO.getPersonName());
        existingPerson.setPersonGenre(clientDTO.getPersonGenre());
        existingPerson.setPersonAge(clientDTO.getPersonAge());
        existingPerson.setPersonAddress(clientDTO.getPersonAddress());
        existingPerson.setPersonPhoneNumber(clientDTO.getPersonPhoneNumber());

        if (existingPerson instanceof Client) {
            Client existingClient = (Client) existingPerson;
            existingClient.setClientState(clientDTO.getClientState());
            existingClient.setClientPassword(clientDTO.getClientPassword());
        } else {
            throw new ResourceNotFoundException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage());
        }
        personRepository.save(existingPerson);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData deleteClient(UserDniDTO userDniDTO)  {
        Person client = personRepository.findByPersonDni(userDniDTO.getUserDni())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage()));
        personRepository.delete(client);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }


    private ClientDTO convertToDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        modelMapper.map(client, clientDTO);
        return clientDTO;
    }
}
