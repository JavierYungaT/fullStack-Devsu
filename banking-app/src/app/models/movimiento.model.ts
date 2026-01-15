

import { Account } from './cuenta.model';

export interface Movement {
  movementId?: number;
  movementDate: string;      // LocalDateTime → ISO string
  movementType: string;
  movementAmount: number;
  movementBalance: number;
  movementState: boolean;
  // accountNumber?: Account;
   accountNumber?: string; 
}