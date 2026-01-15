import { Component, OnInit, ChangeDetectorRef, NgZone  } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';

import { MovimientoService } from '../../services/movimiento.service';
import { CuentaService } from '../../services/cuenta.service';
import { Movement } from '../../models/movimiento.model';
import { Account } from '../../models/cuenta.model';

@Component({
  selector: 'app-movimientos',
  standalone: true, imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './movimientos.components.html',
  styleUrl: './movimientos.components.css'
})
export class MovimientosComponent implements OnInit {

  movements: Movement[] = [];
  filteredMovements: Movement[] = [];
  accounts: Account[] = [];
  searchTerm: string = '';
  showModal: boolean = false;

  isEditMode = false;
  selectedAccountId: number | null = null;

  showNotification: boolean = false;
  notificationMessage: string = '';
  notificationType: 'success' | 'error' = 'success';

  movementForm: FormGroup;

  constructor(
    private movementService: MovimientoService,
    private accountService: CuentaService,
    private fb: FormBuilder,
     private cdr: ChangeDetectorRef, 
    private ngZone: NgZone 
  ) {
    this.movementForm = this.fb.group({
      accountNumber: ['', Validators.required],
      movementType: ['', Validators.required],
      movementAmount: [0, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.loadMovements();
    this.loadAccounts();
  }

  loadMovements(): void {
    this.movementService.getAllMovements().subscribe({
      next: (data) => {
        // this.movements = data || [];
        // this.filteredMovements = data || [];

        this.ngZone.run(() => {
          this.movements = [...data]  ;
          this.filteredMovements = [...data];
          this.cdr.markForCheck();  // Marcar para revisión
        });
      },
      error: (error) => console.error('Error al cargar movimientos:', error)
    });
  }

  loadAccounts(): void {
    this.accountService.getCuentas().subscribe({
      next: (data) => {
        this.accounts = data || [];
      },
      error: (error) => console.error('Error al cargar cuentas:', error)
    });
  }

  onSearch(): void {
    if (!this.searchTerm.trim()) {
      this.filteredMovements = this.movements;
      return;
    }

    const term = this.searchTerm.toLowerCase();

    this.filteredMovements = this.movements.filter(m =>
      m.accountNumber?.toLowerCase().includes(term) ||
      m.movementType?.toLowerCase().includes(term)
    );
  }

  openModal(): void {
    this.showModal = true;
    this.movementForm.reset({
      movementAmount: 0,
      accountNumber: '',
      movementType: ''
    });
  }

  // closeModal(): void {
  //   this.showModal = false;
  //   this.movementForm.reset({
  //     movementAmount: 0,
  //     accountNumber: '',
  //     movementType: ''
  //   });
  // }

  // onSubmit(): void {
  //   if (this.movementForm.invalid) {
  //     Object.values(this.movementForm.controls).forEach(control =>
  //       control.markAsTouched()
  //     );
  //     return;
  //   }

  //   const typeMap: Record<string, string> = {
  //     DEPOSIT: 'Deposito',
  //     WITHDRAWAL: 'Retiro'
  //   };

  //   const movementRequest = {
  //     accountNumber: this.movementForm.value.accountNumber,
  //     movementType: typeMap[this.movementForm.value.movementType],
  //     value: this.movementForm.value.movementAmount
  //   };


  //   this.movementService.createMovement(movementRequest).subscribe({
  //     next: () => {
  //       alert('Movimiento registrado exitosamente');
  //       this.loadMovements();
  //       this.closeModal();
  //     },
  //     error: (error) => {
  //       console.error('Error:', error);
  //       alert(error.error?.message || 'Error al registrar el movimientot');
  //     }
  //   });
  // }



  // Guardamos el movimiento seleccionado para editar
  selectedMovement: Movement | null = null;

  editMovement(mov: Movement) {
    this.selectedMovement = mov;
    this.showModal = true;

    console.log(this.selectedMovement)
    // Cargamos los datos en el formulario
    this.movementForm.setValue({
      accountNumber: mov.accountNumber,
      movementType: mov.movementType === 'Deposito' ? 'DEPOSIT' : 'WITHDRAWAL',
      movementAmount: mov.movementAmount >= 0 ? mov.movementAmount : -mov.movementAmount
    });
  }

  deleteMovement(mov: Movement) {
    if (!confirm('¿Estás seguro de eliminar este movimiento?')) return;

      console.log('ELIMINADO movimiento ID:', mov.movementId);

    this.movementService.deleteMovement(mov.movementId!).subscribe({
      next: () => {
        alert('Movimiento eliminado');
        this.loadMovements();
      },
      error: (err) => {
        console.error(err);
        alert('Error eliminando movimiento');
      }
    });
  }

  // onSubmit(): void {
  //   if (this.movementForm.invalid) {
  //     Object.values(this.movementForm.controls).forEach(control => control.markAsTouched());
  //     return;
  //   }

  //   const typeMap: Record<string, string> = { DEPOSIT: 'Deposito', WITHDRAWAL: 'Retiro' };

  //   const movementRequest = {
  //     // accountNumber: this.movementForm.value.accountNumber,
  //     movementType: typeMap[this.movementForm.value.movementType],
  //     value: this.movementForm.value.movementAmount
  //   };

  //   if (this.selectedMovement) {
  //     // Actualizamos
  //     console.log('Actualizando movimiento ID:', this.selectedMovement.movementId, movementRequest);
  //     this.movementService.updateMovement(this.selectedMovement.movementId!, movementRequest).subscribe({
  //       next: () => {
  //         alert('Movimiento actualizado');
  //         this.loadMovements();
  //         this.closeModal();
  //         this.selectedMovement = null;
  //       },
  //       error: (err) => {
  //         console.error(err);
  //         alert(err.error?.message || 'Error actualizando movimiento');
  //       }
  //     });
  //   } else {
  //     // Creamos
  //     this.movementService.createMovement(movementRequest).subscribe({
  //       next: () => {
  //         alert('Movimiento registrado');
  //         this.loadMovements();
  //         this.closeModal();
  //       },
  //       error: (err) => {
  //         console.error(err);
  //         alert(err.error?.message || 'Error registrando movimiento');
  //       }
  //     });
  //   }
  // }

  onSubmit(): void {
    if (this.movementForm.invalid) {
      Object.values(this.movementForm.controls).forEach(control => control.markAsTouched());
      return;
    }

    const typeMap: Record<string, string> = { DEPOSIT: 'Deposito', WITHDRAWAL: 'Retiro' };

    if (this.selectedMovement) {
      // Actualización
      const movementUpdate = {
        movementType: typeMap[this.movementForm.value.movementType],
        movementAmount: this.movementForm.value.movementAmount
      };

      console.log('Actualizando movimiento ID:', this.selectedMovement.movementId, movementUpdate);

      this.movementService.updateMovement(this.selectedMovement.movementId!, movementUpdate).subscribe({
        next: (response) => {
          // alert('Movimiento actualizado');
          this.showMessage(response.message || 'Movimiento actualizada correctamente', 'success');

          this.loadMovements();
          this.closeModal();
          this.selectedMovement = null;
        },
        error: (err) => {
          console.error(err);
          alert(err.error?.message || 'Error actualizando movimiento');
        }
      });
    } else {
      // Creación
      const movementCreate = {
        accountNumber: this.movementForm.value.accountNumber,
        movementType: typeMap[this.movementForm.value.movementType],
        value: this.movementForm.value.movementAmount
      };

      console.log('Creando movimiento:', movementCreate);

      this.movementService.createMovement(movementCreate).subscribe({
        next: (response) => {
          // alert('Movimiento registrado');
          this.showMessage(response.message || 'Movimiento registrado correctamente', 'success');

          this.loadMovements();
          this.closeModal();
        },
        error: (err) => {
          console.error(err);
          alert(err.error?.message || 'Error registrando movimiento');
        }
      });
    }
  }


  closeModal(): void {
    this.showModal = false;
    this.selectedMovement = null; // reseteamos edición
    this.movementForm.reset({ movementAmount: 0, accountNumber: '', movementType: '' });
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
    const control = this.movementForm.get(field);

    if (control?.hasError('required')) {
      return 'This field is required';
    }
    if (control?.hasError('min')) {
      return 'Amount must be greater than 0';
    }
    return '';
  }
}
