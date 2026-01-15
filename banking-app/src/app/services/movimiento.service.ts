import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movement } from '../models/movimiento.model';
import { environment } from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class MovimientoService {

  // private apiUrl = 'http://localhost:7086/movement';
  private apiUrl = `${environment.apiCuentaUrl}/movement`;

  constructor(private http: HttpClient) { }

  // GET ALL
  getAllMovements(): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}/getAllMovements`);
  }

  // GET BY ACCOUNT NUMBER
  getMovementsByAccount(accountNumber: string): Observable<Movement[]> {
    return this.http.post<Movement[]>(
      `${this.apiUrl}/getMovementByAccount`,
      { accountNumber }
    );
  }

  // GET BY CLIENT DNI
  getMovementsByDni(personDni: string): Observable<Movement[]> {
    return this.http.post<Movement[]>(
      `${this.apiUrl}/getMovementByDni`,
      { personDni }
    );
  }

  // CREATE MOVEMENT (DEPOSIT / WITHDRAW)
  createMovement(data: {
    accountNumber: string;
    movementType: string;
    // movementAmount: number;
    value: number;
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/makeMovement`, data);
  }

  // UPDATE (PATCH)
  // updateMovement(movementId: number, fields: Partial<Movement>): Observable<any> {
  //   return this.http.patch(
  //     `${this.apiUrl}/updateMovement/${movementId}`,
  //     fields
  //   );
  // }

  // DELETE
  // deleteMovement(movementId: number): Observable<any> {
  //   return this.http.delete(`${this.apiUrl}/deleteMovement`, {
  //     body: { movementId }
  //   });
  // }

  updateMovement(movementId: number, fields: any): Observable<any> {
    return this.http.patch(`${this.apiUrl}/updateMovement/${movementId}`, fields);
  }

  deleteMovement(movementId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/deleteMovement`, { body: { movementId } });
  }
}
