import React, {useEffect, useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";

function Example() {

    const [exampleList, setExampleList] = useState([]);
    // useEffect를 동작하게 하기 위한 상태 변경
    const [trigger, setTrigger] = useState(false);

    useEffect(() => {
        axios.get("/api/example")
            .then((res) => {
                console.log(res);

                setExampleList(res.data)
            })
    }, [trigger]);

    const [exampleForm, setExampleForm] = useState({name: "", description: ""});


    const createExample = () => {
        axios.post("/api/example", exampleForm)
            .then(() => {
                setTrigger(prev => !prev);

                alert("created");
            })
            .catch(() => alert("error"))
    }

    const deleteExample = (id) => {
        axios.delete(`/api/example/${id}`)
            .then(() => {
                setTrigger(prev => !prev);
                setExampleForm({name: "", description: ""});
                alert("complete");
            })
            .catch(() => alert("error"))
    }

    return (
        <>
            <div>
                <label htmlFor="name">이름</label>
                <input id="name" value={exampleForm.name}
                       onChange={(e) => setExampleForm({...exampleForm, name: e.target.value})}/>
                <label htmlFor="description">설명</label>
                <input id="description" value={exampleForm.description}
                       onChange={(e) => setExampleForm({...exampleForm, description: e.target.value})}/>
                <button onClick={createExample}>Create Example</button>
            </div>

            {exampleList.map((example) => <div key={example.id}>
                <Link to={`/example/${example.id}`}>
                    <p>
                        memberEmail : {example.memberEmail}
                    </p>
                    <p>
                        id : {example.id}
                    </p>
                    <p>
                        name : {example.name}
                    </p>
                    <p>
                        description : {example.description}
                    </p>
                </Link>
                <button onClick={() => deleteExample(example.id)}>Delete</button>
            </div>)}
        </>
    );
}

export default Example;