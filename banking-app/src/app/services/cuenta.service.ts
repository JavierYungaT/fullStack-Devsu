import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../models/cuenta.model';
import { environment } from '../../environments/environment'; 

@Injectable({
  providedIn: 'root'
})
export class CuentaService {

  // private apiUrl = 'http://localhost:7086/accounts';
  private apiUrl = `${environment.apiCuentaUrl}/accounts`; 

  constructor(private http: HttpClient) { }

  getCuentas(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}/findAllAccounts`);
  }

  getCuentaByNumber(accountNumber: string): Observable<Account> {
    return this.http.post<Account>(
      `${this.apiUrl}/getAccountByNumber`,
      { accountNumber }
    );
  }


  createCuenta(payload: any): Observable<any> {
    return this.http.post(
      'http://localhost:7086/accounts/saveAccount',
      payload
    );
  }

  // updateCuentaParcial(accountId: number, fields: any): Observable<any> {
  //   return this.http.patch(
  //     `${this.apiUrl}/updateAccount/${accountId}`,
  //     fields
  //   );
  // }

  // Actualizar con PATCH (campos individuales)
  updateCuentaParcial(accountId: number, fields: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/updateAccount/${accountId}`, fields);
  }

  updateCuentaCompleta(cuenta: Account): Observable<any> {
    return this.http.put(
      `${this.apiUrl}/updateAccountAllData`,
      cuenta
    );
  }

  deleteCuentaByNumber(accountNumber: string): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/deleteAccount/${accountNumber}`
    );
  }


  
}
