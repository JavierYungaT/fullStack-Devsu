import { Component, OnInit,ChangeDetectorRef, NgZone  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CuentaService } from '../../services/cuenta.service';
import { Account } from '../../models/cuenta.model';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../services/cliente.service';



@Component({
  selector: 'app-cuentas',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './cuentas.components.html',
  styleUrl: './cuentas.components.css'
})
export class CuentasComponent implements OnInit {

  clientes: Cliente[] = [];
  cuentas: Account[] = [];
  cuentasFiltradas: Account[] = [];
  searchTerm = '';
  showModal = false;
  isEditMode = false;
  selectedAccountId: number | null = null;

  showNotification: boolean = false;
  notificationMessage: string = '';
  notificationType: 'success' | 'error' = 'success';

  cuentaForm: FormGroup;

  constructor(
    private cuentaService: CuentaService,
    private clienteService: ClienteService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef, 
    private ngZone: NgZone 
  ) {
    this.cuentaForm = this.fb.group({
      accountNumber: ['', Validators.required],
      accountType: ['', Validators.required],
      accountBalance: [0, [Validators.required, Validators.min(0)]],
      clientIdentification: ['', Validators.required],
      clientName: ['', Validators.required],
      accountState: [true]
    });
  }

  ngOnInit(): void {
    this.loadCuentas();
  }

  // loadClientes(): void {
  //   this.clienteService.getClientes().subscribe({
  //     next: (data) => {
        

  //       this.ngZone.run(() => {
  //         this.clientes = [...data];
  //         this.cdr.markForCheck();  
  //       });
  //     },
  //     error: (error) => {
  //       console.error('Error al cargar clientes:', error);
  //     }
  //   });
  // }

  loadCuentas(): void {
    this.cuentaService.getCuentas().subscribe({
      next: data => {
        // this.cuentas = data;
        // this.cuentasFiltradas = data;

        this.ngZone.run(() => {
          this.cuentas = [...data];
          this.cuentasFiltradas = [...data];
          this.cdr.markForCheck();  // Marcar para revisión
        });
      },
      error: err => console.error(err)
    });
  }

  onSearch(): void {
    if (!this.searchTerm.trim()) {
      this.cuentasFiltradas = this.cuentas;
      return;
    }

    const term = this.searchTerm.toLowerCase();
    this.cuentasFiltradas = this.cuentas.filter(c =>
      c.accountNumber.includes(term) ||
      c.clientName.toLowerCase().includes(term) ||
      c.clientIdentification.includes(term)
    );
  }

  openModal(cuenta?: any): void {
    this.showModal = true;

    if (cuenta) {
      this.isEditMode = true;
      this.selectedAccountId = cuenta.accountId;
      this.cuentaForm.patchValue({
        accountNumber: cuenta.numeroCuenta,
        accountType: cuenta.tipoCuenta,
        accountBalance: cuenta.saldoInicial,
        clientIdentification: cuenta.clienteCedula,
        clientName: cuenta.nombreCliente,
        accountState: cuenta.estado
      });

    } else {
      this.isEditMode = false;
      this.cuentaForm.reset({
        accountBalance: 0,
        accountState: true
      });
    }
  }


  closeModal(): void {
    this.showModal = false;
    this.isEditMode = false;
    this.selectedAccountId = null;
    this.cuentaForm.reset({
      accountBalance: 0,
      accountState: true
    });
  }



  // onSubmit(): void {
  //   if (this.cuentaForm.invalid) {
  //     Object.values(this.cuentaForm.controls).forEach(control =>
  //       control.markAsTouched()
  //     );
  //     return;
  //   }

  //   const payload = {
  //     numeroCuenta: this.cuentaForm.value.accountNumber,
  //     tipoCuenta: this.cuentaForm.value.accountType,
  //     saldoInicial: this.cuentaForm.value.accountBalance,
  //     estado: this.cuentaForm.value.accountState,
  //     identificacion: this.cuentaForm.value.clientIdentification,
  //     nombre: this.cuentaForm.value.clientName
  //   };

  //   this.cuentaService.createCuenta(payload).subscribe({
  //     next: () => {
  //       alert('Cuenta creada correctamente');
  //       this.loadCuentas();
  //       this.closeModal();
  //     },
  //     error: (error) => {
  //       console.error(error);
  //       alert(error.error?.message || 'Error al crear la cuenta');
  //     }
  //   });
  // }

  onSubmit(): void {
    if (this.cuentaForm.invalid) {
      this.cuentaForm.markAllAsTouched();
      return;
    }

    if (this.isEditMode && this.selectedAccountId) {
      const fieldsToUpdate = {
        accountType: this.cuentaForm.value.accountType,
        accountBalance: this.cuentaForm.value.accountBalance,
        accountState: this.cuentaForm.value.accountState,
        clientName: this.cuentaForm.value.clientName
      };

      console.log('Actualizando cuenta ID:', this.selectedAccountId, fieldsToUpdate);

      this.cuentaService.updateCuentaParcial(this.selectedAccountId, fieldsToUpdate).subscribe({
        next: (response) => {
          // alert('Cuenta actualizada correctamente');
          this.showMessage(response.message || 'Cuenta actualizada correctamente', 'success');
          this.loadCuentas();
          this.closeModal();
        },
        error: (error) => {
          console.error('Error al actualizar la cuenta:', error);
          alert(error.error?.message || 'Error al actualizar la cuenta');
        }
      });

    } else {
      // CREAR
      const payload = {
        numeroCuenta: this.cuentaForm.value.accountNumber,
        tipoCuenta: this.cuentaForm.value.accountType,
        saldoInicial: this.cuentaForm.value.accountBalance,
        estado: this.cuentaForm.value.accountState,
        identificacion: this.cuentaForm.value.clientIdentification,
        nombre: this.cuentaForm.value.clientName
      };

      //   const payload = {
  //     numeroCuenta: this.cuentaForm.value.accountNumber,
  //     tipoCuenta: this.cuentaForm.value.accountType,
  //     saldoInicial: this.cuentaForm.value.accountBalance,
  //     estado: this.cuentaForm.value.accountState,
  //     identificacion: this.cuentaForm.value.clientIdentification,
  //     nombre: this.cuentaForm.value.clientName
  //   };

      this.cuentaService.createCuenta(payload).subscribe({
        next: (response) => {
          // alert('Cuenta creada correctamente');
          this.showMessage(response.message || 'Cuenta creada correctamente', 'success');
          this.loadCuentas();
          this.closeModal();
        },
        error: (error) => {
          console.error(error);
          alert(error.error?.message || 'Error al crear la cuenta');
        }
      });
    }
  }


  deleteCuenta(cuenta: Account): void {
  const numero = cuenta.accountNumber || cuenta.numeroCuenta; // tomar lo que exista
  if (!numero) {
    alert('No se pudo identificar el número de cuenta');
    return;
  }

  if (!confirm('¿Está seguro de eliminar esta cuenta?')) return;

  this.cuentaService.deleteCuentaByNumber(numero).subscribe({
    next: (response) => {
      // alert('Cuenta eliminada correctamente');
          this.showMessage(response.message || 'Cuenta eliminada correctamente', 'success');

      this.loadCuentas();
    },
    error: err => {
      console.error('Error al eliminar cuenta:', err);
      alert(err.error?.message || 'Error al eliminar la cuenta');
    }
  });
}

  showMessage(message: string, type: 'success' | 'error' = 'success'): void {
    this.notificationMessage = message;
    this.notificationType = type;
    this.showNotification = true;

    // Auto-ocultar después de 3 segundos
    setTimeout(() => {
      this.showNotification = false;
    }, 3000);
  }

  getErrorMessage(field: string): string {
    const control = this.cuentaForm.get(field);

    if (control?.hasError('required')) {
      return 'Campo obligatorio';
    }
    if (control?.hasError('min')) {
      return `Valor mínimo ${control.errors?.['min'].min}`;
    }
    return '';
  }
}