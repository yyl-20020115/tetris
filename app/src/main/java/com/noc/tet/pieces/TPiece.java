
package com.noc.tet.pieces;

import com.noc.tet.Square;

import android.content.Context;

public class TPiece extends Piece3x3 {

    private final Square tSquare;

    public TPiece(Context c) {
        super(c);
        tSquare = new Square(Piece.type_T, c);
        pattern[1][0] = tSquare;
        pattern[1][1] = tSquare;
        pattern[1][2] = tSquare;
        pattern[2][1] = tSquare;
        reDraw();
    }

    @Override
    public void reset(Context c) {
        super.reset(c);
        pattern[1][0] = tSquare;
        pattern[1][1] = tSquare;
        pattern[1][2] = tSquare;
        pattern[2][1] = tSquare;
        reDraw();
    }
}
