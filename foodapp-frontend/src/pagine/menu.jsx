import React,{useEffect, useState} from 'react'
import { useNavigate } from 'react-router-dom';
function Menu() {
    const [menu, setMenu] = useState([]);
    const [loading, setLoading] = useState(true);
    const [carrello, setCarrello] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
    
        fetch('http://localhost:8080/api/food', {
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(res => {
                if (!res.ok) throw new Error("Errore fetching menu: " + res.status);
                return res.json();
            })
            .then(data => {
                setMenu(data);
                setLoading(false);
            })
            .catch(err => console.error(err));


    }, []); 

            //aggiungi al carrello l'articolo selezionato dal menu
            const handleAddToCart = (food) => {
                setCarrello(prev => [...prev, food]);
                console.log("Carrello aggiornato:", carrello);  
            };
    


    if (loading) return <div className="p-10 text-xl">Caricamento menu...</div>;
    return (
        <>
    <div className="p-10 grid grid-cols-1 md:grid-cols-2  lg:grid-cols-3 gap-6  ">
        {menu.map(food => (
            <div key={food.id} className="border p-4 rounded shadow hover:shadow-lg transition flex flex-col justify-between">
                <div>
                    <img src={food.ImageUrl} alt={food.name} className="w-full h-48 object-cover rounded mb-4" />
                </div>
                <h2 className="text-xl font-bold">{food.name}</h2>
                <p className="text-gray-700">{food.description}</p>
                <div className="mt-4 flex justify-between items-center">
                    <p className="mt-2 font-semibold">€{food.price.toFixed(2)}</p>
                    <button onClick={() => handleAddToCart(food.name)} className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition">
                        Aggiungi al carrello
                    </button>
                </div>
                
            </div>
        ))}
    </div>
    </>
    )
}

export default Menu
