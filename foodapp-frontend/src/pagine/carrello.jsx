import React from "react";
import { useCart } from "./CartContext";

function CarrelloPage() {
    const { carrello, setCarrello } = useCart();

    const handleRemoveItem = (index) => {
        setCarrello(prev => prev.filter((_, i) => i !== index));
    };

    if(carrello.length === 0) return <div className="p-10 text-xl">Il carrello è vuoto</div>;

    return (
        <div className="p-10">
            <h1 className="text-2xl font-bold mb-4">Carrello</h1>
            {carrello.map((item, index) => (
                <div key={index} className="flex justify-between border p-2 rounded mb-2">
                    <p>{item.name} - €{item.price.toFixed(2)}</p>
                    <button onClick={() => handleRemoveItem(index)} className="bg-red-500 text-white px-2 rounded">Rimuovi</button>
                </div>
            ))}
            <div className="flex justify-between mt-4">
                <p className=" mt-4 font-bold">
                    Totale: €{carrello.reduce((sum, item) => sum + item.price, 0).toFixed(2)}
                </p>
                <button className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition">
                    Procedi al pagamento
                </button>
            </div>

        </div>
    );
}

export default CarrelloPage;
