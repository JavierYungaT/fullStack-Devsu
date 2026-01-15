import { Component, OnInit , ChangeDetectorRef, NgZone} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteService, ReporteMovimiento } from '../../services/reporte.service';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';

interface ClientReportDTO {
  clientDni: string;
  clientName: string;
  accounts: AccountReportDTO[];
}

interface AccountReportDTO {
  accountNumber: string;
  accountType: string;
  balance: number;
  movements: MovementDetailDTO[];
}

interface MovementDetailDTO {
  movementDate: string;
  movementType: string;
  movementAmount: number;
  movementBalance: number;
}

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.components.html',
  styleUrl: './reportes.components.css'
})
export class ReportesComponent implements OnInit {
  clientes: Cliente[] = [];
  // clienteSeleccionado: number = 0;
  clienteSeleccionado: Cliente | null = null;
  fechaInicio: string = '';
  fechaFin: string = '';
  reporteGenerado: ClientReportDTO | null = null;
  mostrarReporte: boolean = false;

   showNotification: boolean = false;
  notificationMessage: string = '';
  notificationType: 'success' | 'error' = 'success';

  constructor(
    private reporteService: ReporteService,
    private clienteService: ClienteService,
    private cdr: ChangeDetectorRef, 
    private ngZone: NgZone 
  ) { }

  ngOnInit(): void {
    this.loadClientes();
    this.setFechasDefault();
  }

  // loadClientes(): void {
  //   this.clienteService.getClientes().subscribe({
  //     next: (data) => {
  //       this.clientes = data;
  //     },
  //     error: (error) => {
  //       console.error('Error al cargar clientes:', error);
  //     }
  //   });
  // }

  loadClientes(): void {
    this.clienteService.getClientes().subscribe({
      next: (data) => {
        // console.log('Clientes recibidos:', data);
        // this.clientes = data;

        this.ngZone.run(() => {
          this.clientes = [...data]  ;
          this.cdr.markForCheck();  // Marcar para revisión
        });
      },
      error: (error) => {
        console.error('Error al cargar clientes:', error);
      }
    });
  }


  setFechasDefault(): void {
    const hoy = new Date();
    const hace30Dias = new Date();
    hace30Dias.setDate(hoy.getDate() - 30);

    this.fechaFin = this.formatDate(hoy);
    this.fechaInicio = this.formatDate(hace30Dias);
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }



  // generarReporte(): void {
  //   if (!this.clienteSeleccionado || !this.fechaInicio || !this.fechaFin) {
  //     alert('Por favor complete todos los campos');
  //     return;
  //   }


  //   console.log('Cliente seleccionado:', this.clienteSeleccionado.personDni);
  //   // AGREGAR ESTOS LOGS:
  //   console.log('🔍 DNI enviado:', this.clienteSeleccionado.personDni);
  //   console.log('🔍 Tipo de dato:', typeof this.clienteSeleccionado.personDni);
  //   const body = {
  //     clientDni: this.clienteSeleccionado.personDni,
  //     initialDate: this.fechaInicio,
  //     finalDate: this.fechaFin
  //   };

  //   console.log('🔍 Body completo:', body);


  //   this.reporteService.generarReporte(body).subscribe({
  //     next: (data) => {
  //       this.reporteGenerado = data;
  //       this.mostrarReporte = true;
  //     },
  //     error: (error) => {
  //       console.error('Error al generar reporte:', error);
  //       alert('Error al generar el reporte');
  //     }
  //   });
  // }

  generarReporte(): void {
    if (!this.clienteSeleccionado || !this.fechaInicio || !this.fechaFin) {
      alert('Por favor complete todos los campos');
      return;
    }

    console.log('🔍 DNI enviado:', this.clienteSeleccionado.personDni);

    const body = {
      clientDni: this.clienteSeleccionado.personDni,
      initialDate: this.fechaInicio,
      finalDate: this.fechaFin
    };

    console.log('🔍 Body completo:', body);

    this.reporteService.generarReporte(body).subscribe({
      next: (data: ClientReportDTO) => { 
        
        // this.reporteGenerado = data;
        // this.mostrarReporte = true;
        this.ngZone.run(() => {
          this.reporteGenerado = data;
          this.mostrarReporte = true;
          this.cdr.markForCheck();  // Marcar para revisión
        });

      },
      error: (error) => {
        console.error('Error al generar reporte:', error);
        alert('Error al generar el reporte');
      }
    });
  }


  descargarPDF(): void {
    if (!this.clienteSeleccionado || !this.fechaInicio || !this.fechaFin) {
      alert('Debe generar un reporte primero');
      return;
    }

    const body = {
      clientDni: this.clienteSeleccionado.personDni,
      initialDate: this.fechaInicio,
      finalDate: this.fechaFin
    };

    this.reporteService.descargarPDF(body).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `reporte_${this.clienteSeleccionado?.personName || 'cliente'}_${this.fechaInicio}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Error al descargar PDF:', error);
        alert('Error al descargar el PDF');
      }
    });
  }

  // getNombreCliente(): string {
  //   const cliente = this.clientes.find(c => c.personId === this.clienteSeleccionado);
  //   return cliente ? cliente.personName : '';
  // }

  noHayMovimientos(): boolean {
    if (!this.reporteGenerado || !this.reporteGenerado.accounts) {
      return true;
    }

    if (this.reporteGenerado.accounts.length === 0) {
      return true;
    }

    return this.reporteGenerado.accounts.every(account =>
      !account.movements || account.movements.length === 0
    );
  }

  getNombreCliente(): string {
    return this.clienteSeleccionado ? this.clienteSeleccionado.personName : '';
  }

}