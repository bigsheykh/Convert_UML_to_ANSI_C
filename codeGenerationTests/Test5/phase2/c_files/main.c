#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

#define N	200000

void srand_() {
	struct timeval tv;

	gettimeofday(&tv, NULL);
	srand(tv.tv_sec ^ tv.tv_usec);
}

int rand_(int n) {
	return (rand() * 76543LL + rand()) % n;
}

int compare(const void *a, const void *b) {
	int ia = *(int *) a;
	int ib = *(int *) b;

	return ia - ib;
}

void sort(int *aa, int n) {
	int i;

	for (i = 0; i < n; i++) {
		int j = rand_(i + 1), tmp;

		tmp = aa[i], aa[i] = aa[j], aa[j] = tmp;
	}
	qsort(aa, n, sizeof *aa, compare);
}

int main() {
	int t;

	srand_();
	scanf("%d", &t);
	while (t--) {
		static int aa[N], dp[N];
		int n, p, k, i;

		scanf("%d%d%d", &n, &p, &k);
		for (i = 0; i < n; i++)
			scanf("%d", &aa[i]);
		sort(aa, n);
		for (i = 0; i < k - 1; i++)
			dp[i] = (i == 0 ? 0 : dp[i - 1]) + aa[i];
		dp[k - 1] = aa[k - 1];
		for (i = k; i < n; i++)
			dp[i] = aa[i] + dp[i - k];
		for (k = n; k > 0; k--)
			if (dp[k - 1] <= p)
				break;
		printf("%d\n", k);
	}
	return 0;
}
