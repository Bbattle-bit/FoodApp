import React, { useEffect, useState } from 'react';
import { useCart } from "./CartContext";

function Menu() {
    const [menu, setMenu] = useState([]);
    const [loading, setLoading] = useState(true);
    const { carrello, setCarrello } = useCart();  // prendi carrello dal context
    const [showNotLogged, setShowNotLogged] = useState(false); // stato per il popup

    useEffect(() => {
        const token = localStorage.getItem("token");
        fetch('http://localhost:8080/api/food', {
            headers: { "Authorization": "Bearer " + token }
        })
        .then(res => res.ok ? res.json() : Promise.reject("Errore fetching menu"))
        .then(data => { setMenu(data); setLoading(false); })
        .catch(err => console.error(err));
    }, []);

    const handleAddToCart = (food) => {
        const token = localStorage.getItem("token");
        if(!token){
            setShowNotLogged(true); // mostra il popup
            return;
        }
        setCarrello(prev => [...prev, food]);
    };

    // fa sparire il popup dopo 3 secondi
    useEffect(() => {
        if(showNotLogged){
            const timer = setTimeout(() => setShowNotLogged(false), 3000);
            return () => clearTimeout(timer);
        }
    }, [showNotLogged]);

    if (loading) return <div className="p-10 text-xl">Caricamento menu...</div>;

    return (
        <div className="relative">
            {/* Popup NotLogged */}
            {showNotLogged && (
                <div className="fixed inset-0 bg-transparent flex justify-center items-center z-50">
                    <div className="bg-red-400 p-6 rounded shadow-lg max-w-sm text-center">
                        <p className="font-semibold text-gray-800">Devi essere loggato per aggiungere al carrello!</p>
                        <button 
                            onClick={() => setShowNotLogged(false)} 
                            className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                        >
                            Chiudi
                        </button>
                    </div>
                </div>
            )}

            {/* Menu */}
            <div className="p-10 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {menu.map(food => (
                    <div key={food.id} className="border p-4 rounded shadow hover:shadow-lg transition flex flex-col justify-between">
                        <img src={food.ImageUrl} alt={food.name} className="w-full h-48 object-cover rounded mb-4" />
                        <h2 className="text-xl font-bold">{food.name}</h2>
                        <p className="text-gray-700">{food.description}</p>
                        <div className="mt-4 flex justify-between items-center">
                            <p className="mt-2 font-semibold">€{food.price.toFixed(2)}</p>
                            <button 
                                onClick={() => handleAddToCart(food)} 
                                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition cursor-pointer"
                            >
                                Aggiungi al carrello

                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Menu;
