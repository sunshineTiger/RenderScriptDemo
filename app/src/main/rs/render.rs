#pragma version(1)
#pragma rs java_package_name(render.test)
#pragma rs_fp_relaxed
#include "rs_debug.rsh"
rs_allocation gIntFrame;
float radians_rot;
float widht;
float height;

void init(){

}

uchar4 __attribute__((kernel)) xxxinvert(uchar4 in, uint32_t x, uint32_t y) {
        uchar4 out = in;
        rs_matrix4x4 matrixa;
        rs_matrix4x4 matrixb;
        rs_matrix4x4 matrixc;
        rsMatrixLoadRotate (&matrixa, radians_rot, 0, 0, 1);//正：顺时针  负：逆时针
        rsMatrixLoadTranslate(&matrixb,-(widht-1)/2,-(height-1)/2,0 );
        rsMatrixLoadTranslate(&matrixc,(height-1)/2,(widht-1)/2,0 );
        float4 pointA;
        pointA.x=x;
        pointA.y=y;
        pointA.z=1;
        pointA.w=1;
        float4 changePointA= rsMatrixMultiply(&matrixb,pointA);//平移
        float4 changePointB= rsMatrixMultiply(&matrixa,changePointA);//旋转
        float4 changePointC= rsMatrixMultiply(&matrixc,changePointB);//平移
        rsSetElementAt_uchar4(gIntFrame,out,(int)changePointC.x,(int)changePointC.y);
        return out;
}
