#pragma version(1)
#pragma rs java_package_name(com.android.example.hellocompute)

float width;
float height;

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
    float h = x / width;
    float v = 1.0 - y / height;
    float3 out;

    if (h < 1.0/6) {
        out.r = v;
        out.g = 0 + ((v - 0) * h * 6);
        out.b = 0;
    } else if (h < 2.0/6) {
        out.r = v - ((v - 0) * (h - 1.0/6) * 6);
        out.g = v;
        out.b = 0;
    } else if (h < 3.0/6) {
        out.r = 0;
        out.g = v;
        out.b = 0 + ((v - 0) * (h - 2.0/6) * 6);
    } else if (h < 4.0/6) {
        out.r = 0;
        out.g = v - ((v - 0) * (h - 3.0/6) * 6);
        out.b = v;
    } else if (h < 5.0/6) {
        out.r = 0 + ((v - 0) * (h - 4.0/6) * 6);
        out.g = 0;
        out.b = v;
    } else {
        out.r = v;
        out.g = 0;
        out.b = v - ((v - 0) * (h - 5.0/6) * 6);
    }

    *v_out = rsPackColorTo8888(out);
}