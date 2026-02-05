import React,{useEffect, useState} from 'react'
 
function Menu() {
    const [menu, setMenu] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('http://localhost:8080/api/food')
            .then(res => res.json())
            .then(data => {
                setMenu(data);
                setLoading(false);
            })
            .catch(err => console.error('Error fetching menu:', err));
    }, []);

    if (loading) return <div className="p-10 text-xl">Caricamento menu...</div>;
    return (
<div className="p-10 grid grid-cols-2  lg:grid-cols-3 gap-6 ">
      {menu.map(food => (
        <div key={food.id} className="border p-4 rounded shadow hover:shadow-lg transition">
            <div>
                <img src={food.ImageUrl} alt={food.name} className="w-full h-48 object-cover rounded mb-4" />
            </div>
            <h2 className="text-xl font-bold">{food.name}</h2>
            <p className="text-gray-700">{food.description}</p>
            <p className="mt-2 font-semibold">€{food.price.toFixed(2)}</p>
        </div>
      ))}
    </div>

    )
}

export default Menu
