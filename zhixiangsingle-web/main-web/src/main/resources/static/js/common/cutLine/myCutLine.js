function update(array) {

    var fill = window.getComputedStyle(document.querySelector(".myCutLineHeader"),null).getPropertyValue("background-color");
    ctx.fillStyle = fill;
    ctx.globalCompositeOperation = "source-over";
    ctx.fillRect(0,0,cvs.width,cvs.height);
    ctx.globalCompositeOperation = "screen";

    ctx2.fillStyle = fill;
    ctx2.globalCompositeOperation = "source-over";
    ctx2.fillRect(0,0,cvs2.width,cvs2.height);
    ctx2.globalCompositeOperation = "screen";
    for (var i = 0; i < waves.length; i++) {
        for (var j = 0; j < waves[i].nodes.length; j++) {
            bounce(waves[i].nodes[j]);
        }
        drawWave(waves[i]);
    }
    ctx.globalCompositeOperation = "hue";
    ctx.fillStyle = fill;

    ctx2.globalCompositeOperation = "hue";
    ctx2.fillStyle = fill;
}

function wave(colour,lambda,nodes) {
    this.colour = colour;
    this.lambda = lambda;
    this.nodes = [];
    var tick = 1;
    for (var i = 0; i <= nodes+2; i++) {
        var temp = [(i-1)*cvs.width/nodes,0,Math.random()*200,.3];//this.speed*plusOrMinus
        this.nodes.push(temp);
    }
    waves.push(this);
}

function bounce(node) {
    node[1] = waveHeight/2*Math.sin(node[2]/20)+cvs.height/2;
    node[2] = node[2] + node[3];

}

function drawWave (obj) {
    var diff = function(a,b) {
        return (b - a)/2 + a;
    }
    ctx.fillStyle = obj.colour;
    ctx.beginPath();
    ctx.moveTo(0,cvs.height);
    ctx.lineTo(obj.nodes[0][0],obj.nodes[0][1]);

    ctx2.fillStyle = obj.colour;
    ctx2.beginPath();
    ctx2.moveTo(0,cvs2.height);
    ctx2.lineTo(obj.nodes[0][0],obj.nodes[0][1]);
    for (var i = 0; i < obj.nodes.length; i++) {
        if (obj.nodes[i+1]) {
            ctx.quadraticCurveTo(
                obj.nodes[i][0],obj.nodes[i][1],
                diff(obj.nodes[i][0],obj.nodes[i+1][0]),diff(obj.nodes[i][1],obj.nodes[i+1][1])
            );

            ctx2.quadraticCurveTo(
                obj.nodes[i][0],obj.nodes[i][1],
                diff(obj.nodes[i][0],obj.nodes[i+1][0]),diff(obj.nodes[i][1],obj.nodes[i+1][1])
            );
        }else{
            ctx.lineTo(obj.nodes[i][0],obj.nodes[i][1]);
            ctx.lineTo(cvs.width,cvs.height);

            ctx2.lineTo(obj.nodes[i][0],obj.nodes[i][1]);
            ctx2.lineTo(cvs2.width,cvs2.height);
        }

    }
    ctx.closePath();
    ctx.fill();

    ctx2.closePath();
    ctx2.fill();
}

function resizeCanvas(canvas,width,height) {
    if (width && height) {
        canvas.width = width;
        canvas.height = height;
    } else {
        if (window.innerHeight > 1920) {
            canvas.width = window.innerWidth;
        }
        else {
            canvas.width = 1920;
        }

        canvas.height = waveHeight;
    }

}