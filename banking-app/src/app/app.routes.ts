import { Routes } from '@angular/router';

import { ClientesComponent } from './components/clientes/clientes.components';
import { CuentasComponent } from './components/cuentas/cuentas.components';
import { MovimientosComponent } from './components/movimientos/movimientos.components';
import { ReportesComponent } from './components/reportes/reportes.components';

export const routes: Routes = [
  { path: '', redirectTo: '/clientes', pathMatch: 'full' },
  { path: 'clientes', component: ClientesComponent },
  { path: 'cuentas', component: CuentasComponent },
  { path: 'movimientos', component: MovimientosComponent },
  { path: 'reportes', component: ReportesComponent },
  { path: '**', redirectTo: '/clientes' }
];