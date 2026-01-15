import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'; 


export interface ReporteMovimiento {
  fecha: string;
  cliente: string;
  numeroCuenta: string;
  tipo: string;
  saldoInicial: number;
  estado: boolean;
  movimiento: number;
  saldoDisponible: number;
}

export interface ClientReportDTO {
  clientDni: string;
  clientName: string;
  accounts: AccountReportDTO[];
}

export interface AccountReportDTO {
  accountNumber: string;
  accountType: string;
  balance: number;
  movements: MovementDetailDTO[];
}

export interface MovementDetailDTO {
  movementDate: string;
  movementType: string;
  movementAmount: number;
  movementBalance: number;
}
@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private apiUrl = 'http://localhost:7086/movement';
  // private apiUrl = `${environment.apiCuentaUrl}/movement`;


  constructor(private http: HttpClient) { }



  // generarReporte(body: { clientDni: string, initialDate: string, finalDate: string }): Observable<ReporteMovimiento[]> {
  //   return this.http.post<ReporteMovimiento[]>(`${this.apiUrl}/generateReport`, body);
  // }


  generarReporte(body: any): Observable<ClientReportDTO> {  // ✅ CAMBIAR ESTO
    return this.http.post<ClientReportDTO>(`${this.apiUrl}/generateReport`, body);
  }


  descargarPDF(body: any): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/generateReportPdf`, body, {
      responseType: 'blob'
    });
  }
}