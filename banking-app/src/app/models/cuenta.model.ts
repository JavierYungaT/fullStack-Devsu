
import { Movement } from './movimiento.model';

export interface Account  {
  accountId?: number;
  accountNumber: string;
  accountType: string;
  accountBalance: number;
  clientIdentification: string;
  clientName: string;
  accountState: boolean;
  movements?: Movement[];


  // VIENEN DEL BACKEND

  numeroCuenta?: string;
  tipoCuenta?: string;
  saldoInicial?: number;
  clienteCedula?: string;
  estado?: boolean;
  nombreCliente?: string;
}

