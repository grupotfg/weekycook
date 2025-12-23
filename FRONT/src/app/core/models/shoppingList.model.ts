// Elemento calculado para la lista de la compra (ListaCompraItemDto)
export interface ShoppingListItem {
  ingredienteId: number;
  nombre: string;
  cantidadTotal: number;
  unidad: string;
}

// Estructura genérica para agrupar la lista completa si el backend devuelve metadatos
export interface ShoppingList {
  items: ShoppingListItem[];
}
