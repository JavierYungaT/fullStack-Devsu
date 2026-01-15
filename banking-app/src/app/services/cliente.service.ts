import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { environment } from '../../environments/environment'; 

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private apiUrl = 'http://localhost:7085/clientes';
  // private apiUrl = `${environment.apiCuentaUrl}/clientes`; 

  constructor(private http: HttpClient) {}

  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.apiUrl}/findAllClients`);
  }

  createCliente(cliente: Cliente): Observable<any> {
    return this.http.post(`${this.apiUrl}/saveClient`, cliente);
  }

  // ✅ PATCH REAL
  updateClienteParcial(userId: number, fields: Partial<Cliente>): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/updateClient/${userId}`,
      fields
    );
  }

  // deleteClienteByDni(dni: string): Observable<any> {
  //   return this.http.delete(`${this.apiUrl}/deleteClient`, {
  //     body: { personDni: dni }
  //   });
  // }

  deleteClienteByDni(dni: string): Observable<any> {
  return this.http.delete(
    `http://localhost:7085/clientes/deleteClient/${dni}`
  );
}

}
