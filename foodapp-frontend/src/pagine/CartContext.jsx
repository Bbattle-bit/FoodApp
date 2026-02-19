import { createContext, useContext, useState, useEffect } from "react";

const CartContext = createContext();

export function CartProvider({ children }) {
    const [carrello, setCarrello] = useState(() => {
        // carica dal localStorage all'avvio
        const saved = localStorage.getItem("carrello");
        return saved ? JSON.parse(saved) : [];
    });

    // ogni volta che il carrello cambia, salva nel localStorage
    useEffect(() => {
        localStorage.setItem("carrello", JSON.stringify(carrello));
    }, [carrello]);

    return (
        <CartContext.Provider value={{ carrello, setCarrello }}>
            {children}
        </CartContext.Provider>
    );
}

export const useCart = () => useContext(CartContext);
