import React from 'react'
import { useNavigate } from 'react-router-dom';
import { useEffect,useState } from 'react';
function Gestione_ordini() {

    const navigate = useNavigate();

    const [ordini, setOrdini] = useState([]);
    const [loading, setLoading] = useState(true);
    // Controllo del token JWT e del ruolo dell'utente
    useEffect(() =>{
        const token = localStorage.getItem("token");
        if(!token){
            navigate("/LogIn");
            return;
        }try{
            // Decodifica del token JWT per ottenere il ruolo dell'utente
            const payload = JSON.parse(atob(token.split('.')[1]));
            if(payload.role !== "ADMIN"){
                navigate("/LogIn");
            }
        }catch(err){
            console.error("Token JWT non valido:", err);
            navigate("/LogIn");
        }

        //chiamata API ordini
        fetch('http://localhost:8080/api/orders', {
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(res =>{
                if(!res.ok) throw new Error("Errore fetching ordini: " + res.status);
                return res.json();
            })
            .then(data =>{
                setOrdini(data);
                setLoading(false);
            })
            .catch(err =>{
                console.error(err);
            })
    },[navigate]);

    // Funzione per gestire il cambio di stato dell'ordine
    const handleStatusChange = (orderId, newStatus) => {
        const token = localStorage.getItem("token");
    
        fetch(`http://localhost:8080/api/orders/${orderId}/status?status=${newStatus}`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
        .then(res => {
            if(!res.ok) throw new Error("Errore aggiornamento status: " + res.status);
            return res.json();
        })
        .then(updatedOrder => {
            // Aggiorna lo stato localmente per non fare refresh
            setOrdini(prevOrdini =>
                prevOrdini.map(o => o.id === updatedOrder.id ? updatedOrder : o)
            );
        })
        .catch(err => {
            console.error(err);
            alert("Impossibile aggiornare lo status");
        });
    };
    

    // Funzione per gestire l'eliminazione di un ordine
    const handleDeleteOrder = (orderId) => {
        if (!window.confirm("Sei sicuro di voler eliminare questo ordine?")) return;
    
        const token = localStorage.getItem("token");
    
        fetch(`http://localhost:8080/api/orders/${orderId}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
        .then(res => {
            if(!res.ok) throw new Error("Errore eliminazione ordine: " + res.status);
            // Aggiorna lo stato locale rimuovendo l'ordine eliminato
            setOrdini(prevOrdini => prevOrdini.filter(o => o.id !== orderId));
        })
        .catch(err => {
            console.error(err);
            alert("Impossibile eliminare l'ordine");
        });
    };
    
    if(loading) return <div className="p-10 text-xl">Caricamento ordini...</div>;

    return (
        <div>
            <h1 className="text-center">Gestione Ordini</h1>
            <div className="p-10 grid grid-cols-1 md:grid-cols-2  lg:grid-cols-3 gap-6 ">
                
                {ordini.map(ordine => (
                    <div key={ordine.id} className="border p-4 rounded shadow hover:shadow-lg transition">
                        <h2 className="text-xl font-bold">{ordine.user.username}</h2>
                        <p className="text-sm font-bold">INDIRIZZO DI CONSEGNA: {ordine.user.address}</p>
                        <p className="text-sm font-bold">CITTA': {ordine.user.city}</p>
                    <select
                        value={ordine.status}
                        onChange={(e) => handleStatusChange(ordine.id, e.target.value)}
                        className="border rounded p-1 mt-2"
                    >
                        <option value="IN_ATTESA_DI_CONFERMA">In attesa di conferma dal locale</option>
                        <option value="ACCETTATO">Ordine accettato</option>
                        <option value="IN_PREPARAZIONE">in preparazione</option>
                        <option value="IN_CONSEGNA">in consegna</option>
                        <option value="CONSEGNATO">Consegnato</option>
                        <option value="CANCELLATO">Ordine cancellato</option>
                    </select>
                            <div>
                            {ordine.items.map(item => (
                                <p key={item.id}>{item.foodItem.name} x {item.quantity} - ${item.price}</p>
                            ))}
                        </div>
                        <p className="text-green-300-700">TOTALE: {ordine.totalAmount}$</p>         

                        <button onClick={() => handleDeleteOrder(ordine.id)} className="bg-red-500 text-white px-3 py-1 rounded mt-2 hover:bg-red-700">
                            Elimina Ordine
                        </button>
  
                    </div>
                ),)             
                }

            </div>
        </div>
    )
}

export default Gestione_ordini
