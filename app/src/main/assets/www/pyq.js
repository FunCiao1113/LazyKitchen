window.onload = function () {
    father = document.getElementById("father");
    for (let i = 0; i < 4; i++) {
        let card = document.createElement("div");
        card.setAttribute("class", "card")
        let small_head = document.createElement("img");
        small_head.setAttribute("class", "small_head");
        small_head.setAttribute("src", "head.jpg");
        small_head.setAttribute("alt", "加载错误");
        let time = document.createElement("time");
        time.setAttribute("class", "date");
        time.innerHTML += "2020.6.6";
        let p = document.createElement("p");
        p.innerHTML += "3333";
        p.setAttribute("class","text");
        let images = document.createElement("div");
        images.setAttribute("class", "images");
        for (let i = 0; i < 5; i++) {
            let content = document.createElement("img");
            content.setAttribute("class", "content");
            content.setAttribute("src", "cat.jpg");
            content.setAttribute("alt", "加载错误");
            images.appendChild(content);
        }
        let share = document.createElement("button");
        share.setAttribute("class", "share");
        share.innerHTML += "分享"
        card.appendChild(small_head);
        card.appendChild(time);
        card.appendChild(p);
        card.appendChild(images);
        card.appendChild(share);
        father.append(card);
    }

}