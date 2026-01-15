import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from '../../services/cliente.service';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './clientes.components.html',
  styleUrl: './clientes.components.css'
})
export class ClientesComponent implements OnInit {
  clientes: Cliente[] = [];
  clientesFiltrados: Cliente[] = [];
  searchTerm: string = '';
  showModal: boolean = false;
  clienteForm: FormGroup;
  isEditMode: boolean = false;
  selectedClienteId: number | null = null;
  showNotification: boolean = false;
  notificationMessage: string = '';
  notificationType: 'success' | 'error' = 'success';

  constructor(
    private clienteService: ClienteService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef, 
    private ngZone: NgZone 
  ) {
    this.clienteForm = this.fb.group({
      personName: ['', Validators.required],
      personGenre: ['', Validators.required],
      personAge: [null, Validators.required],
      personDni: ['', Validators.required],
      personAddress: ['', Validators.required],
      personPhoneNumber: ['', Validators.required],
      clientPassword: ['', Validators.required],
      clientState: [true]
    });
  }

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(): void {
    this.clienteService.getClientes().subscribe({
      next: (data) => {
        // Ejecutar dentro de NgZone para asegurar la detección
        this.ngZone.run(() => {
          this.clientes = [...data];
          this.clientesFiltrados = [...data];
          this.cdr.markForCheck();  // Marcar para revisión
        });
      },
      error: (error) => {
        console.error('Error al cargar clientes:', error);
      }
    });
  }

  onSearch(): void {
    if (!this.searchTerm.trim()) {
      this.clientesFiltrados = [...this.clientes];
      return;
    }

    const term = this.searchTerm.toLowerCase();
    this.clientesFiltrados = this.clientes.filter(cliente =>
      cliente.personName.toLowerCase().includes(term) ||
      cliente.personPhoneNumber.includes(term) ||
      cliente.personDni.includes(term)
    );
  }

  openModal(cliente?: Cliente): void {
    this.showModal = true;

    if (cliente) {
      this.isEditMode = true;
      this.selectedClienteId = cliente.personId!;

      this.clienteForm.patchValue({
        personName: cliente.personName,
        personGenre: cliente.personGenre,
        personAge: cliente.personAge,
        personDni: cliente.personDni,
        personAddress: cliente.personAddress,
        personPhoneNumber: cliente.personPhoneNumber,
        clientPassword: cliente.clientPassword,
        clientState: cliente.clientState
      });
    } else {
      this.isEditMode = false;
      this.selectedClienteId = null;
      this.clienteForm.reset({ clientState: true });
    }
  }

  closeModal(): void {
    this.showModal = false;
    this.clienteForm.reset({ clientState: true });
    this.isEditMode = false;
    this.selectedClienteId = null;
  }

  // onSubmit(): void {
  //   if (this.clienteForm.invalid) {
  //     this.clienteForm.markAllAsTouched();
  //     return;
  //   }

  //   const payload = {
  //     personName: this.clienteForm.value.personName,
  //     personGenre: this.clienteForm.value.personGenre,
  //     personAge: this.clienteForm.value.personAge,
  //     personDni: this.clienteForm.value.personDni,
  //     personAddress: this.clienteForm.value.personAddress,
  //     personPhoneNumber: this.clienteForm.value.personPhoneNumber,
  //     clientPassword: this.clienteForm.value.clientPassword,
  //     clientState: this.clienteForm.value.clientState
  //   };

  //   if (this.isEditMode && this.selectedClienteId) {
  //     // ACTUALIZAR
  //     this.clienteService.updateClienteParcial(
  //       this.selectedClienteId,
  //       payload
  //     ).subscribe({
  //       next: () => {
  //         alert('Cliente actualizado exitosamente');
  //         this.closeModal();
  //         this.loadClientes();  // ⬅️ Recargar después de cerrar modal
  //       },
  //       error: err => {
  //         console.error(err);
  //         alert('Error al actualizar el cliente');
  //       }
  //     });
  //   } else {
  //     // CREAR
  //     this.clienteService.createCliente(payload as any).subscribe({
  //       next: () => {
  //         alert('Cliente creado exitosamente');
  //         this.closeModal();
  //         this.loadClientes();  // ⬅️ Recargar después de cerrar modal
  //       },
  //       error: err => {
  //         console.error(err);
  //         alert('Error al crear el cliente');
  //       }
  //     });
  //   }
  // }


  onSubmit(): void {
  if (this.clienteForm.invalid) {
    this.clienteForm.markAllAsTouched();
    return;
  }

  const payload = {
    personName: this.clienteForm.value.personName,
    personGenre: this.clienteForm.value.personGenre,
    personAge: this.clienteForm.value.personAge,
    personDni: this.clienteForm.value.personDni,
    personAddress: this.clienteForm.value.personAddress,
    personPhoneNumber: this.clienteForm.value.personPhoneNumber,
    clientPassword: this.clienteForm.value.clientPassword,
    clientState: this.clienteForm.value.clientState
  };

  if (this.isEditMode && this.selectedClienteId) {
    // ACTUALIZAR
    this.clienteService.updateClienteParcial(
      this.selectedClienteId,
      payload
    ).subscribe({
      next: (response) => {
        
        // alert(response.message || 'Cliente actualizado exitosamente');
         this.showMessage(response.message || 'Cliente actualizado exitosamente', 'success');
        this.closeModal();
        this.loadClientes();
      },
      error: (error) => {
        console.error('Error completo:', error);
       
        const errorMessage = error.error?.message || 'Error al actualizar el cliente';
        alert(errorMessage);
      }
    });
  } else {
    // CREAR
    this.clienteService.createCliente(payload as any).subscribe({
      next: (response) => {
        this.showMessage(response.message || 'Cliente creado exitosamente', 'success');
        this.closeModal();
        this.loadClientes();
      },
      error: (error) => {
        console.error('Error completo:', error);
        // ⬇️ Capturar mensaje de error del backend
        const errorMessage = error.error?.message || 'Error al crear el cliente';
        alert(errorMessage);
      }
    });
  }
}

  // deleteCliente(cliente: Cliente): void {
  //   if (confirm(`¿Está seguro de eliminar a ${cliente.personName}?`)) {
  //     this.clienteService.deleteClienteByDni(cliente.personDni).subscribe({
  //       next: () => {
  //         alert('Cliente eliminado exitosamente');
  //         this.loadClientes();  // ⬅️ Recargar datos
  //       },
  //       error: (error) => {
  //         console.error('Error:', error);
  //         alert('Error al eliminar el cliente');
  //       }
  //     });
  //   }
  // }

  deleteCliente(cliente: Cliente): void {
  if (confirm(`¿Está seguro de eliminar a ${cliente.personName}?`)) {
    this.clienteService.deleteClienteByDni(cliente.personDni).subscribe({
      next: (response) => {
         this.showMessage(response.message || 'Cliente eliminado exitosamente', 'success');
        this.loadClientes();
      },
      error: (error) => {
        console.error('Error completo:', error);
        // ⬇️ Mensaje de error del backend
        const errorMessage = error.error?.message || 'Error al eliminar el cliente';
        alert(errorMessage);
      }
    });
  }
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


  getErrorMessage(fieldName: string): string {
    const control = this.clienteForm.get(fieldName);

    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (control?.hasError('minlength')) {
      const required = control.errors?.['minlength'].requiredLength;
      return `Mínimo ${required} caracteres`;
    }
    if (control?.hasError('min')) {
      return `Edad mínima: ${control.errors?.['min'].min}`;
    }

    return '';
  }
}