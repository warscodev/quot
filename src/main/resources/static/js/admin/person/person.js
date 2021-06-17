function yearMaxLength(e){
    if(e.value.length > e.maxLength){
        e.value = e.value.slice(0, e.maxLength);
    }
}

function monthMaxLength(e){
    if(e.value.length > e.maxLength){
        e.value = e.value.slice(0, e.maxLength);
    }else if(e.value > 12){
        e.value = e.value.slice(0, 1);
    }
}

function dayMaxLength(e){
    if(e.value.length > e.maxLength){
        e.value = e.value.slice(0, e.maxLength);
    }else if(e.value > 31){
        e.value = e.value.slice(0, 1);
    }
}

