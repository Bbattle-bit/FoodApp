import { createContext, useContext, useState } from "react";

// Crea il contesto
const CartContext = createContext();

// Provider da mettere intorno alle tue Routes
export function CartProvider({ children }) {
    const [carrello, setCarrello] = useState([]);
    return (
        <CartContext.Provider value={{ carrello, setCarrello }}>
            {children}
        </CartContext.Provider>
    );
}

// Hook comodo per usare il carrello in ogni componente
export function useCart() {
    return useContext(CartContext);
}
