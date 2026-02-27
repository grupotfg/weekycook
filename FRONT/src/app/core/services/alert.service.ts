import { Injectable } from '@angular/core';
import Swal, { SweetAlertIcon } from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  
  // nuestros colores de WeekyCook
  private confirmButtonColor = '#6610f2'; // primary
  private cancelButtonColor = '#ff5c5c';  // danger

  // 1. Mensaje de Éxito (Ej: Receta guardada)
  success(title: string, text?: string) {
    return Swal.fire({
      title,
      text,
      icon: 'success',
      confirmButtonColor: this.confirmButtonColor,
      timer: 2000, // Se cierra solo a los 2 segundos
      showConfirmButton: false
    });
  }

  // 2. Mensaje de Error ( x ej: Fallo en el login)
  error(title: string, text?: string) {
    return Swal.fire({
      title,
      text,
      icon: 'error',
      confirmButtonColor: this.confirmButtonColor
    });
  }

  // 3. Confirmación Crítica (x ej: Eliminar receta, categoría o usuario)
  // esto devuelve una Promesa para que se sepa si el usuario puso que sí
  confirmDelete(itemName: string): Promise<boolean> {
    return Swal.fire({
      title: '¿Estás seguro?',
      text: `Vas a eliminar: ${itemName}. Esta acción no se puede deshacer.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: this.cancelButtonColor, // El botón de acción peligrosa en rojo
      cancelButtonColor: '#6c757d', // Gris para cancelar
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      return result.isConfirmed;
    });
  }
}