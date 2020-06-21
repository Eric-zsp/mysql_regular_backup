/**
 * Created by Eric.Zhang on 2017/5/11.
 */

function photoCompressSize(_convas) {
    var convertCanvas = $('<canvas></canvas>').get(0);
    var context = convertCanvas.getContext('2d');
    convertCanvas.width=160;
    convertCanvas.height=160;
    context.drawImage(_convas,0, 0, 160, 160);

    return convertCanvas;
}